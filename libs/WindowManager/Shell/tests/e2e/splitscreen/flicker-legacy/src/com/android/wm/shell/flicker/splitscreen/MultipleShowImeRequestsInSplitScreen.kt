/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.wm.shell.flicker.splitscreen

import android.platform.test.annotations.Presubmit
import android.tools.NavBar
import android.tools.Rotation
import android.tools.ScenarioBuilder
import android.tools.flicker.junit.FlickerParametersRunnerFactory
import android.tools.flicker.legacy.FlickerBuilder
import android.tools.flicker.legacy.LegacyFlickerTest
import android.tools.traces.SERVICE_TRACE_CONFIG
import android.tools.traces.component.ComponentNameMatcher
import androidx.test.filters.RequiresDevice
import com.android.wm.shell.flicker.splitscreen.benchmark.MultipleShowImeRequestsInSplitScreenBenchmark
import com.android.wm.shell.flicker.utils.ICommonAssertions
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.junit.runners.Parameterized

/**
 * Test quick switch between two split pairs.
 *
 * To run this test: `atest WMShellFlickerTestsSplitScreenGroupOther:MultipleShowImeRequestsInSplitScreen`
 */
@RequiresDevice
@RunWith(Parameterized::class)
@Parameterized.UseParametersRunnerFactory(FlickerParametersRunnerFactory::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MultipleShowImeRequestsInSplitScreen(override val flicker: LegacyFlickerTest) :
        MultipleShowImeRequestsInSplitScreenBenchmark(flicker), ICommonAssertions {
    override val transition: FlickerBuilder.() -> Unit
        get() = {
            defaultSetup(this)
            defaultTeardown(this)
            thisTransition(this)
        }

    @Presubmit
    @Test
    fun imeLayerAlwaysVisible() =
            flicker.assertLayers {
                this.isVisible(ComponentNameMatcher.IME)
            }

    companion object {
        private fun createFlickerTest(
            navBarMode: NavBar
        ) = LegacyFlickerTest(ScenarioBuilder()
            .withStartRotation(Rotation.ROTATION_0)
            .withEndRotation(Rotation.ROTATION_0)
            .withNavBarMode(navBarMode), resultReaderProvider = { scenario ->
            android.tools.flicker.datastore.CachedResultReader(
                scenario, SERVICE_TRACE_CONFIG
            )
        })

        @Parameterized.Parameters(name = "{0}")
        @JvmStatic
        fun getParams() = listOf(
            createFlickerTest(NavBar.MODE_GESTURAL),
            createFlickerTest(NavBar.MODE_3BUTTON)
        )
    }
}