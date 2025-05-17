package com.example.user.app.application.auth.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.Objects;

@Data
@Embeddable
public class Device {

    private String deviceId;    // 브라우저나 앱에서 생성한 UUID
    private String platform;    // WEB, ANDROID, IOS
    private String browser;     // Chrome, Safari 등

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(deviceId, device.deviceId) && Objects.equals(platform, device.platform) && Objects.equals(browser, device.browser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, platform, browser);
    }
}
