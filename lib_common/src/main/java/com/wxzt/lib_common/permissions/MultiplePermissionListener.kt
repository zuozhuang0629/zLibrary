/*
 * Copyright (C) 2015 Karumi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wxzt.lib_common.permissions

import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class MultiplePermissionListener(private val permissionListener: IPermissionListener) : MultiplePermissionsListener {
    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
        for (response in report.grantedPermissionResponses) {
            permissionListener.showPermissionGranted(response.permissionName)
        }

        for (response in report.deniedPermissionResponses) {
            permissionListener.showPermissionDenied(response.permissionName, response.isPermanentlyDenied)
        }
    }

    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
        permissionListener.showPermissionRationale(token)
    }


}