module com.hiddewieringa.elevator {
    requires org.axonframework.messaging;
    requires org.axonframework.modelling;
    requires org.axonframework.spring;
    requires org.axonframework.eventsourcing;
    requires kotlin.stdlib;
    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires jackson.annotations;
    requires spring.data.commons;
    requires spring.boot.autoconfigure;
    requires slf4j.api;
    requires spring.boot;
    requires reactor.core;
    requires spring.webflux;
}
