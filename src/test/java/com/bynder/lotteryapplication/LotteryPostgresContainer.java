package com.bynder.lotteryapplication;

import org.testcontainers.containers.PostgreSQLContainer;

public class LotteryPostgresContainer extends PostgreSQLContainer<LotteryPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:11.1";
    private static LotteryPostgresContainer container;

    private LotteryPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static LotteryPostgresContainer getInstance() {
        if (container == null) {
            container = new LotteryPostgresContainer();
            container.start();
        }
        if(! container.isRunning()) {
            container.start();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
