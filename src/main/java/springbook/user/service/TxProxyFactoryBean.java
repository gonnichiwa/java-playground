package springbook.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    private Object target;
    private PlatformTransactionManager transactionManager;
    private Class<?> serviceInterface;
    private String pattern;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler(); // 프록시에서 타겟(TestUserService)이 뭔지 전달해줌.
        txHandler.setTransactionManager(this.transactionManager);
        txHandler.setTarget(this.target);
        txHandler.setPattern(this.pattern);
        return Proxy.newProxyInstance(
                TransactionHandler.class.getClassLoader(),
                new Class[] {serviceInterface}, // 다이내믹 프록시(Proxy.newProxyInstance)가 구현해야할 인터페이스
                txHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
