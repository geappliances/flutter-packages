// Copyright 2013 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package io.flutter.plugins.camera.features.exposurelock;

import android.annotation.SuppressLint;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import androidx.annotation.NonNull;
import io.flutter.plugins.camera.CameraProperties;
import io.flutter.plugins.camera.features.CameraFeature;

/** Controls whether or not the exposure mode is currently locked or automatically metering. */
public class ExposureLockFeature extends CameraFeature<ExposureMode> {
  @NonNull private ExposureMode currentSetting = ExposureMode.auto;

  /**
   * Creates a new instance of the {@see ExposureLockFeature}.
   *
   * @param cameraProperties Collection of the characteristics for the current camera device.
   */
  public ExposureLockFeature(@NonNull CameraProperties cameraProperties) {
    super(cameraProperties);
  }

  @NonNull
  @Override
  public String getDebugName() {
    return "ExposureLockFeature";
  }

  @SuppressLint("KotlinPropertyAccess")
  @NonNull
  @Override
  public ExposureMode getValue() {
    return currentSetting;
  }

  @Override
  public void setValue(@NonNull ExposureMode value) {
    this.currentSetting = value;
  }

  // Available on all devices.
  @Override
  public boolean checkIsSupported() {
    return true;
  }

  @Override
  public void updateBuilder(@NonNull CaptureRequest.Builder requestBuilder) {
    if (!checkIsSupported()) {
      return;
    }

    if (currentSetting == ExposureMode.locked) {
      requestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
      requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_OFF);
      requestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, (long)128);
      return;
    } else {
      requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CameraMetadata.CONTROL_AE_MODE_ON);
    }

    requestBuilder.set(CaptureRequest.CONTROL_AE_LOCK, currentSetting == ExposureMode.locked);
  }
}
