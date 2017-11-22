# fatalis
Spring-boot项目fatalis，通过maven多模块依赖管理：
    1.fatalis-parent为父项目，管理pom.xml依赖；
    2.fatalis-webapp为web项目，展示层为html5，支持nginx动静分离，分布式war包部署；
    3.fatalis-webcore为项目核心依赖，提供基础类，工具类和拦截等，jar包依赖；
    4.fatalis-quartz为定时任务，通过schedule设定定时，支持更新cron，单服务jar包部署；

    *.项目整合数据源druid，读写分离，持久层mybatis，mybatis-plus，缓存redis，分布式spring-session等。
