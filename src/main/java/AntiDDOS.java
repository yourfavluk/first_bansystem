public class AntiDDOS {
    private Map<String, Integer> connectionsPerIP = new HashMap<>();
    private Map<String, Long> connectionDelays = new HashMap<>();

    public boolean canConnect(SocketAddress address) {
        String ip = address.toString().split("/")[1];
        int maxConnectionsPerIP = 5; // Max connections per IP
        long connectionDelay = 5000; // Connection delay in milliseconds

        // Check if IP is already connected too many times
        if (connectionsPerIP.getOrDefault(ip, 0) >= maxConnectionsPerIP) {
            return false;
        }

        // Check if IP has a connection delay
        if (connectionDelays.containsKey(ip) && System.currentTimeMillis() < connectionDelays.get(ip)) {
            return false;
        }

        // Update connection count and delay
        connectionsPerIP.put(ip, connectionsPerIP.getOrDefault(ip, 0) + 1);
        connectionDelays.put(ip, System.currentTimeMillis() + connectionDelay);

        return true;
    }

    public void removeConnection(SocketAddress address) {
        String ip = address.toString().split("/")[1];
        connectionsPerIP.put(ip, connectionsPerIP.getOrDefault(ip, 0) - 1);
    }
}