package com.rafakob.nsdhelper;

import android.net.nsd.NsdServiceInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;

/**
 * A class representing service information for network service discovery.
 */
public class NsdService implements Parcelable {
    public static final Creator<NsdService> CREATOR = new Creator<NsdService>() {
        public NsdService createFromParcel(Parcel source) {
            return new NsdService(source);
        }

        public NsdService[] newArray(int size) {
            return new NsdService[size];
        }
    };
    private final String name;
    private final String type;
    private final String hostIp;
    private final String hostName;
    private final int port;
    private final InetAddress host;

    public NsdService(NsdServiceInfo nsdServiceInfo) {
        this.name = nsdServiceInfo.getServiceName();
        this.type = nsdServiceInfo.getServiceType();
        this.hostIp = nsdServiceInfo.getHost() == null ? null : nsdServiceInfo.getHost().getHostAddress();
        this.hostName = nsdServiceInfo.getHost() == null ? null : nsdServiceInfo.getHost().getHostName();
        this.port = nsdServiceInfo.getPort();
        this.host = nsdServiceInfo.getHost();
    }

    public NsdService(String name, String type, String hostIp, String hostName, int port, InetAddress host) {
        this.name = name;
        this.type = type;
        this.hostIp = hostIp;
        this.hostName = hostName;
        this.port = port;
        this.host = host;
    }

    protected NsdService(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        this.hostIp = in.readString();
        this.hostName = in.readString();
        this.port = in.readInt();
        this.host = (InetAddress) in.readSerializable();
    }

    /**
     * Returns service name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns service type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns host ip address.
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * Returns host domain name.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Returns service port number.
     */
    public int getPort() {
        return port;
    }

    /**
     * Retruns host information.
     */
    public InetAddress getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", hostIp='" + hostIp + '\'' +
                ", hostName='" + hostName + '\'' +
                ", port=" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NsdService that = (NsdService) o;

        if (port != that.port) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (hostIp != null ? !hostIp.equals(that.hostIp) : that.hostIp != null) return false;
        return hostName != null ? hostName.equals(that.hostName) : that.hostName == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (hostIp != null ? hostIp.hashCode() : 0);
        result = 31 * result + (hostName != null ? hostName.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.hostIp);
        dest.writeString(this.hostName);
        dest.writeInt(this.port);
        dest.writeSerializable(this.host);
    }
}
