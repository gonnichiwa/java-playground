import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration // 설정파일 이름 지정 안하면 클래스이름 + "-context.xml"이 디폴트 이름으로 사용됨.
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;
    @Test
    public void getMessageFromFactoryBean() {
        Object message = context.getBean("message");
        assertThat(message, is(Message.class)); // message타입이 Message.class인가?
        assertThat(((Message)message).getText(), is("Factory Bean"));

        Object message2 = context.getBean("&message"); // "&" + bean id 하면서 팩토리빈 자체를 돌려줌.
        assertThat(message2, is(MessageFactoryBean.class));
    }
}

class MessageFactoryBean implements FactoryBean<Message> {
    private String text;
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text); // 여기에 다이내믹 프록시 (Proxy.newProxyInstance()) 넣어주면?
    }
    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }
    @Override
    public boolean isSingleton() {
        return false;
    }
}
class Message {
    private final String text;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }
}
