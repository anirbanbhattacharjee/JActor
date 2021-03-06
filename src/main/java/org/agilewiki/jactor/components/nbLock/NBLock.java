/*
 * Copyright 2012 Bill La Forge
 *
 * This file is part of AgileWiki and is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License (LGPL) as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * or navigate to the following url http://www.gnu.org/licenses/lgpl-2.1.txt
 *
 * Note however that only Scala, Java and JavaScript files are being covered by LGPL.
 * All other files are covered by the Common Public License (CPL).
 * A copy of this license is also included and can be
 * found as well at http://www.opensource.org/licenses/cpl1.0.txt
 */
package org.agilewiki.jactor.components.nbLock;

import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.bind.Internals;
import org.agilewiki.jactor.bind.MethodBinding;
import org.agilewiki.jactor.components.Component;

import java.util.ArrayDeque;

/**
 * <p>Non-blocking Lock.</p>
 * <p>Note that the technique used here,
 * of saving an RP for subsequent use,
 * should only be used with AsyncRequest messages.
 * Otherwise exception handling will not work correctly.</p>
 */
public final class NBLock extends Component {
    private ArrayDeque<RP<Object>> deque = new ArrayDeque<RP<Object>>();

    /**
     * Bind request classes.
     *
     * @throws Exception Any exceptions thrown while binding.
     */
    @Override
    public void bindery() throws Exception {
        thisActor.bind(Lock.class.getName(), new MethodBinding<Lock, Object>() {
            @Override
            public void processRequest(Internals internals, Lock request, RP<Object> rp)
                    throws Exception {
                deque.addLast(rp);
                if (deque.size() == 1) {
                    rp.processResponse(null);
                }
            }
        });

        thisActor.bind(Unlock.class.getName(), new MethodBinding<Unlock, Object>() {
            @Override
            public void processRequest(Internals internals, Unlock request, RP<Object> rp)
                    throws Exception {
                deque.removeFirst();
                rp.processResponse(null);
                RP<Object> rp1 = deque.peekFirst();
                if (rp1 != null) {
                    rp1.processResponse(null);
                }
            }
        });
    }
}
