package bobrowskim.moquette.issue;

import io.moquette.broker.Server;
import io.moquette.broker.config.FileResourceLoader;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.IResourceLoader;
import java.io.IOException;
import java.util.Properties;

public class MoquetteServer {
    
    private Server server;
    private boolean running;

    public MoquetteServer() {
        this.server = new Server();
    }
    
    public void start() throws IOException {
        System.out.println("Starting server..");
        this.server.startServer(Config.instance());
        this.running = true;
        System.out.println("Server is running");
    }
    
    public void stop() {
        if (this.running) {
            System.out.println("Stopping server...");
            this.server.stopServer();
            this.running = false;
            System.out.println("Server stopped");
        }
    }
    
    private static class Config extends IConfig {
        
        private final Properties properties = new Properties();
        
        Config() {
            setProperty("port", "1883");
            setProperty("host", "127.0.0.1");
            setProperty("allow_anonymous", "true");
            setProperty("allow_zero_byte_client_id", "true");
        }
        
        static Config instance() {
            return new Config();
        }
        
        @Override
        public void setProperty(String s, String s1) {
            properties.setProperty(s, s1);
        }
        
        @Override
        public String getProperty(String s) {
            return properties.getProperty(s);
        }
        
        @Override
        public String getProperty(String s, String s1) {
            return properties.getProperty(s, s1);
        }
        
        @Override
        public IResourceLoader getResourceLoader() {
            return new FileResourceLoader();
        }
    }
    
}
