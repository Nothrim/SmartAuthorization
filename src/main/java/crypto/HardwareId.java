package crypto;

import com.google.common.hash.Hashing;
import oshi.SystemInfo;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Collectors;

public class HardwareId {
    private static final String APPEND = "ORID";

    public static String getId() throws UnsupportedEncodingException {
        return new String(Base64.getEncoder().encode(Hashing.sha256().hashString(getMachineKey(), StandardCharsets.UTF_8).asBytes()), "UTF-8");
    }

    private static String getMachineKey() {
        SystemInfo si = new SystemInfo();
        return (si.getOperatingSystem().getManufacturer()
                + si.getOperatingSystem().getNetworkParams().getIpv4DefaultGateway()
                + si.getOperatingSystem().getNetworkParams().getIpv6DefaultGateway()
                + Arrays.stream(si.getHardware().getNetworkIFs()).map(e -> e.getDisplayName() + e.getMacaddr()).collect(Collectors.joining())
                + si.getHardware().getMemory().getTotal()
                + si.getHardware().getComputerSystem().getBaseboard().getManufacturer() + APPEND);
    }

    public static boolean check(String key) throws UnsupportedEncodingException {
        return Arrays.equals(Base64.getDecoder().decode(key), Base64.getDecoder().decode(getId()));
    }
}
