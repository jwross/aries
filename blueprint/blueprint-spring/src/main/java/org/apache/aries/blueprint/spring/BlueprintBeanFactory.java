/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.aries.blueprint.spring;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.aries.blueprint.ComponentDefinitionRegistry;
import org.apache.aries.blueprint.reflect.BeanMetadataImpl;
import org.apache.aries.blueprint.reflect.RefMetadataImpl;
import org.apache.aries.blueprint.reflect.ValueMetadataImpl;
import org.apache.aries.blueprint.services.ExtendedBlueprintContainer;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.blueprint.container.NoSuchComponentException;
import org.osgi.service.blueprint.container.ReifiedType;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.ResolvableType;

public class BlueprintBeanFactory extends DefaultListableBeanFactory {

    private final ExtendedBlueprintContainer container;

    public BlueprintBeanFactory(ExtendedBlueprintContainer container) {
        super(new WrapperBeanFactory(container));
        this.container = container;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return super.getBean(requiredType);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        ComponentDefinitionRegistry registry = container.getComponentDefinitionRegistry();
        ComponentMetadata metadata = registry.getComponentDefinition(beanName);
        if (metadata != null && !(metadata instanceof SpringMetadata)) {
            throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
                    "Cannot register bean definition [" + beanDefinition + "] for bean '" + beanName +
                            "': There is already bound.");
        }
        super.registerBeanDefinition(beanName, beanDefinition);
        if (!beanDefinition.isAbstract()) {
            registry.registerComponentDefinition(new SpringMetadata(beanName));
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        super.removeBeanDefinition(beanName);
    }

    public class SpringMetadata extends BeanMetadataImpl {
        private final String beanName;

        public SpringMetadata(String beanName) {
            this.beanName = beanName;
            setFactoryComponent(new RefMetadataImpl(BlueprintNamespaceHandler.SPRING_BEAN_FACTORY_ID));
            setFactoryMethod("getBean");
            addArgument(new ValueMetadataImpl(beanName), null, -1);
        }

        public BeanDefinition getDefinition() {
            return getBeanDefinition(beanName);
        }

        @Override
        public String getId() {
            return beanName;
        }

        @Override
        public String getScope() {
            return getDefinition().isSingleton() ? SCOPE_SINGLETON : SCOPE_PROTOTYPE;
        }

        @Override
        public int getActivation() {
            return getDefinition().isLazyInit() ? ACTIVATION_LAZY : ACTIVATION_EAGER;
        }

        @Override
        public List<String> getDependsOn() {
            String[] dependson = getDefinition().getDependsOn();
            return dependson != null ? Arrays.asList(dependson) : Collections.<String>emptyList();
        }
    }


    static class WrapperBeanFactory implements BeanFactory {

        private final ExtendedBlueprintContainer container;

        public WrapperBeanFactory(ExtendedBlueprintContainer container) {
            this.container = container;
        }

        @Override
        public Object getBean(String name) throws BeansException {
            try {
                return container.getComponentInstance(name);
            } catch (NoSuchComponentException e) {
                throw new NoSuchBeanDefinitionException(name);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
            Object bean = getBean(name);
            if (requiredType != null && bean != null && !requiredType.isAssignableFrom(bean.getClass())) {
                try {
                    bean = container.getConverter().convert(bean, new ReifiedType(requiredType));
                } catch (Exception ex) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                }
            }
            return (T) bean;
        }

        @Override
        public <T> T getBean(Class<T> requiredType) throws BeansException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object getBean(String name, Object... args) throws BeansException {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsBean(String name) {
            return container.getComponentIds().contains(name);
        }

        @Override
        public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
            throw new UnsupportedOperationException();
        }

        @Override
        public String[] getAliases(String name) {
            throw new UnsupportedOperationException();
        }
    }
}
