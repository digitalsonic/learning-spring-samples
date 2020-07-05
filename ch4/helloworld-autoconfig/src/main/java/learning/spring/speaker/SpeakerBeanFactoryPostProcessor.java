package learning.spring.speaker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

public class SpeakerBeanFactoryPostProcessor
        implements BeanFactoryPostProcessor, EnvironmentAware {
    private static final Log log =
            LogFactory.getLog(SpeakerBeanFactoryPostProcessor.class);
    // 为了获得配置属性，注入Environment
    private Environment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        // 获取属性值
        String enable = environment.getProperty("spring.speaker.enable");
        String language = environment.getProperty("spring.speaker.language", "English");
        String clazz = "learning.spring.speaker." + language + "Speaker";

        // 开关为true则注册Bean，否则结束
        if (!"true".equalsIgnoreCase(enable)) {
            return;
        }
        // 如果目标类不存在，结束处理
        if (!ClassUtils.isPresent(clazz,
                SpeakerBeanFactoryPostProcessor.class.getClassLoader())) {
            return;
        }

        if (beanFactory instanceof BeanDefinitionRegistry) {
            registerBeanDefinition((BeanDefinitionRegistry) beanFactory, clazz);
        } else {
            registerBean(beanFactory, clazz);
        }
    }

    // 如果是BeanDefinitionRegistry，可以注册BeanDefinition
    private void registerBeanDefinition(BeanDefinitionRegistry beanFactory,
                                        String clazz) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(clazz);
        beanFactory.registerBeanDefinition("speaker", beanDefinition);
    }

    // 如果只能识别成ConfigurableListableBeanFactory，直接注册一个Bean实例
    private void registerBean(ConfigurableListableBeanFactory beanFactory,
                              String clazz) {
        try {
            Speaker speaker = (Speaker) ClassUtils.forName(clazz,
                    SpeakerBeanFactoryPostProcessor.class.getClassLoader())
                    .getDeclaredConstructor().newInstance();
            beanFactory.registerSingleton("speaker", speaker);
        } catch (Exception e) {
            log.error("Can not create Speaker.", e);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
