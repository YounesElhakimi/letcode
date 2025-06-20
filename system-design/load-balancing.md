# Load Balancing

Load balancing is the process of distributing network or application traffic across multiple servers to ensure reliability and performance.

## Types of Load Balancing
- **Round Robin:** Requests are distributed sequentially.
- **Least Connections:** Directs traffic to the server with the fewest active connections.
- **IP Hash:** Uses a hash of the client's IP to determine the server.

## Java Example: Simple Round Robin
```java
import java.util.*;
class RoundRobinLB {
    private final List<String> servers;
    private int index = 0;
    public RoundRobinLB(List<String> servers) { this.servers = servers; }
    public String getServer() {
        String server = servers.get(index);
        index = (index + 1) % servers.size();
        return server;
    }
}
```

## Best Practices
- Monitor server health
- Use sticky sessions if needed
- Scale horizontally as demand grows 