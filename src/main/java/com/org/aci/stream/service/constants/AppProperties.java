package com.org.aci.stream.service.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class AppProperties {

    private String schema_registry_url;
    private String kafka_bootstrap_servers;
    private String kafka_security_protocol;
    private String kafka_sasl_kerberos_service_name;
    private String kafka_ssl_truststore_location;
    private String kafka_ssl_truststore_password;
    private String kafka_producer_topics;
    private String java_security_krb5_conf;
    private String principal;
    private String keyTab;

    private String jdbc_url;
    private String jdbc_usr;
    private String jdbc_pwd;


    public String getSchema_registry_url() {
        return schema_registry_url;
    }

    public void setSchema_registry_url(String schema_registry_url) {
        this.schema_registry_url = schema_registry_url;
    }

    public String getKafka_bootstrap_servers() {
        return kafka_bootstrap_servers;
    }

    public void setKafka_bootstrap_servers(String kafka_bootstrap_servers) {
        this.kafka_bootstrap_servers = kafka_bootstrap_servers;
    }

    public String getKafka_security_protocol() {
        return kafka_security_protocol;
    }

    public void setKafka_security_protocol(String kafka_security_protocol) {
        this.kafka_security_protocol = kafka_security_protocol;
    }

    public String getKafka_sasl_kerberos_service_name() {
        return kafka_sasl_kerberos_service_name;
    }

    public void setKafka_sasl_kerberos_service_name(String kafka_sasl_kerberos_service_name) {
        this.kafka_sasl_kerberos_service_name = kafka_sasl_kerberos_service_name;
    }

    public String getKafka_ssl_truststore_location() {
        return kafka_ssl_truststore_location;
    }

    public void setKafka_ssl_truststore_location(String kafka_ssl_truststore_location) {
        this.kafka_ssl_truststore_location = kafka_ssl_truststore_location;
    }

    public String getKafka_ssl_truststore_password() {
        return kafka_ssl_truststore_password;
    }

    public void setKafka_ssl_truststore_password(String kafka_ssl_truststore_password) {
        this.kafka_ssl_truststore_password = kafka_ssl_truststore_password;
    }

    public String getKafka_producer_topics() {
        return kafka_producer_topics;
    }

    public void setKafka_producer_topics(String kafka_producer_topics) {
        this.kafka_producer_topics = kafka_producer_topics;
    }

    public String getJava_security_krb5_conf() {
        return java_security_krb5_conf;
    }

    public void setJava_security_krb5_conf(String java_security_krb5_conf) {
        this.java_security_krb5_conf = java_security_krb5_conf;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getKeyTab() {
        return keyTab;
    }

    public void setKeyTab(String keyTab) {
        this.keyTab = keyTab;
    }

    public String getJdbc_url() {
        return jdbc_url;
    }

    public void setJdbc_url(String jdbc_url) {
        this.jdbc_url = jdbc_url;
    }

    public String getJdbc_usr() {
        return jdbc_usr;
    }

    public void setJdbc_usr(String jdbc_usr) {
        this.jdbc_usr = jdbc_usr;
    }

    public String getJdbc_pwd() {
        return jdbc_pwd;
    }

    public void setJdbc_pwd(String jdbc_pwd) {
        this.jdbc_pwd = jdbc_pwd;
    }
}
