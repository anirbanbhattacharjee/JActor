package org.agilewiki.jactor.lpc.calculator;

import org.agilewiki.jactor.JAIterator;
import org.agilewiki.jactor.Mailbox;
import org.agilewiki.jactor.RP;
import org.agilewiki.jactor.lpc.JLPCActor;

public class FactorialCalculation extends JLPCActor {
    public FactorialCalculation(Mailbox mailbox) {
        super(mailbox);
    }

    @Override
    protected void processRequest(final Object request, final RP rp)
            throws Exception {
        final Calculator calculator = new Calculator(getMailbox());
        send(calculator, new Set(1), new RP() {
            @Override
            public void processResponse(Object response) throws Exception {
                JAIterator it = new JAIterator() {
                    Factorial factorial = (Factorial) request;
                    int max = factorial.getValue();
                    int count = 0;

                    @Override
                    protected void process(final RP rp1) throws Exception {
                        if (count == max) {
                            send(calculator, new Get(), rp1);
                            return;
                        }
                        count += 1;
                        send(calculator, new Multiply(count), new RP() {
                            @Override
                            public void processResponse(Object response) throws Exception {
                                rp1.processResponse(null);
                            }
                        });
                    }
                };
                it.iterate(rp);
            }
        });
    }
}
