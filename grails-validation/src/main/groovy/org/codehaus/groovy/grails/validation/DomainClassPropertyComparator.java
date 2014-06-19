/* Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.validation;

import java.util.Comparator;
import java.util.Map;

import grails.validation.Constrained;
import grails.core.GrailsDomainClass;
import grails.core.GrailsDomainClassProperty;
import org.springframework.util.Assert;

/**
 * Comparator that uses the domain class property constraints to establish order in sort methods and always
 * places the id first.
 *
 * @author Graeme Rocher
 *
 */
@SuppressWarnings("rawtypes")
public class DomainClassPropertyComparator implements Comparator {

    private Map constrainedProperties;
    private GrailsDomainClass domainClass;

    public DomainClassPropertyComparator(GrailsDomainClass domainClass) {
        Assert.notNull(domainClass, "Argument 'domainClass' is required!");

        constrainedProperties = domainClass.getConstrainedProperties();
        this.domainClass = domainClass;
    }

    public int compare(Object o1, Object o2) {
        if (o1.equals(domainClass.getIdentifier())) {
            return -1;
        }
        if (o2.equals(domainClass.getIdentifier())) {
            return 1;
        }

        GrailsDomainClassProperty prop1 = (GrailsDomainClassProperty)o1;
        GrailsDomainClassProperty prop2 = (GrailsDomainClassProperty)o2;

        Constrained cp1 = (Constrained)constrainedProperties.get(prop1.getName());
        Constrained cp2 = (Constrained)constrainedProperties.get(prop2.getName());

        if (cp1 == null & cp2 == null) {
            return prop1.getName().compareTo(prop2.getName());
        }

        if (cp1 == null) {
            return 1;
        }

        if (cp2 == null) {
            return -1;
        }

        if (cp1.getOrder() > cp2.getOrder()) {
            return 1;
        }

        if (cp1.getOrder() < cp2.getOrder()) {
            return -1;
        }

        return 0;
    }
}
