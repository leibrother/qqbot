package cc.rapidev.qqbot.common.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author leibrother
 */
public class ServiceRegistrationCenterTest {

    static class ServiceA {

    }

    static class ServiceB extends ServiceA {

    }

    @Test
    public void testServiceUse() {
        ServiceA serviceA = new ServiceA();
        ServiceB serviceB = new ServiceB();

        ServiceRegistrationCenter center = new ServiceRegistrationCenter();
        center.add(serviceA);
        center.add(serviceB);

        assertEquals(center.use(ServiceB.class), serviceB);
        assertEquals(center.use(ServiceA.class), serviceB);
    }

}