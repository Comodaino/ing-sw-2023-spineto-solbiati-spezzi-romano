package View;

import Distributed.AbstractClient;

public class PassParameters {
    private static AbstractClient client;
    private static State state;

    public static void setClient(AbstractClient client) {
        PassParameters.client = client;
    }

    public static void setState(State state) {
        PassParameters.state = state;
    }

    public static AbstractClient getClient() {
        return client;
    }

    public static State getState() {
        return state;
    }
}
