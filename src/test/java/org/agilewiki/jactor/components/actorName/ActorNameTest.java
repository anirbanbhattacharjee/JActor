package org.agilewiki.jactor.components.actorName;

import junit.framework.TestCase;
import org.agilewiki.jactor.JAMailboxFactory;
import org.agilewiki.jactor.MailboxFactory;
import org.agilewiki.jactor.components.Include;
import org.agilewiki.jactor.components.JCActor;

public class ActorNameTest extends TestCase {
    public void test() {
        MailboxFactory mailboxFactory = JAMailboxFactory.newMailboxFactory(1);
        try {
            JCActor a = new JCActor(mailboxFactory.createMailbox());
            (new Include(ActorName.class)).call(a);
            (new SetActorName("foo")).call(a);
            String nm = (new GetActorName()).call(a);
            System.out.println(nm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mailboxFactory.close();
        }
    }
}
