package com.example.user.app.application.auth.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @NotBlank(message = "deviceId is required")
    private String deviceId;    // 브라우저나 앱에서 생성한 UUID

    @NotBlank(message = "platform is required")
    private String platform;    // WEB, ANDROID, IOS

    @NotBlank(message = "browser is required")
    private String browser;     // Chrome, Safari 등

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(deviceId, device.deviceId)
                && Objects.equals(platform, device.platform)
                && Objects.equals(browser, device.browser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, platform, browser);
    }
}
