/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.fon.veterinarska.server.configuration;

/**
 *
 * @author nenad
 */
public final class Configuration {
    private String DATABASE_URL;
    private String DATABASE_NAME;
    private String DATABASE_USER;
    private String DATABASE_PASSWORD;

    private static final Configuration INSTANCE = new Configuration("localhost:3306", "", "root", "");

    private Configuration(String DATABASE_URL, String DATABASE_NAME, String DATABASE_USER, String DATABASE_PASSWORD) {
        this.DATABASE_URL = DATABASE_URL;
        this.DATABASE_NAME = DATABASE_NAME;
        this.DATABASE_USER = DATABASE_USER;
        this.DATABASE_PASSWORD = DATABASE_PASSWORD;
    }

    public static Configuration getInstance() {
        return INSTANCE;
    }
}
