package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.cophuc.data.CostumeDataRepository
import com.example.cophuc.data.model.*
import com.example.cophuc.ui.CostumeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Cổ Phục Việt", appName)
    }

    @Test
    fun `test cultural compatibility checker detects nhat binh and non ba tam mismatch`() {
        val nhatBinh = CostumeDataRepository.costumes.first { it.id == "nhat_binh" }
        val nonBaTam = CostumeDataRepository.accessories.first { it.id == "acc_non_ba_tam" }

        val result = CostumeDataRepository.checkCompatibility(nhatBinh, listOf(nonBaTam))
        assertEquals(CompatibilityGrade.HISTORICAL_MISMATCH, result.grade)
    }

    @Test
    fun `test cultural compatibility checker approves nhat binh and khan vanh day`() {
        val nhatBinh = CostumeDataRepository.costumes.first { it.id == "nhat_binh" }
        val khanVanh = CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" }
        val kiengVang = CostumeDataRepository.accessories.first { it.id == "acc_kieng_vang" }

        val result = CostumeDataRepository.checkCompatibility(nhatBinh, listOf(khanVanh, kiengVang))
        assertEquals(CompatibilityGrade.PERFECT_HARMONY, result.grade)
    }

    @Test
    fun `test costume selection and accessory toggling`() {
        val viewModel = CostumeViewModel()

        // Costume selection
        val tuThan = CostumeDataRepository.costumes.first { it.id == "tu_than" }
        viewModel.selectCostume(tuThan)
        assertEquals("tu_than", viewModel.uiState.value.activeCostume.id)

        // Accessory toggling
        val quatTram = CostumeDataRepository.accessories.first { it.id == "acc_quat_tram" }
        viewModel.toggleAccessory(quatTram)
        assertNotNull(viewModel.uiState.value.selectedAccessories.find { it.id == "acc_quat_tram" })
    }

    @Test
    fun `test costume 4 directional view angles and labels`() {
        val front = com.example.cophuc.ui.components.CostumeView.FRONT
        val back = com.example.cophuc.ui.components.CostumeView.BACK
        val left = com.example.cophuc.ui.components.CostumeView.LEFT
        val right = com.example.cophuc.ui.components.CostumeView.RIGHT

        assertEquals("Trước", front.label)
        assertEquals(0f, front.angle)

        assertEquals("Sau", back.label)
        assertEquals(180f, back.angle)

        assertEquals("Trái", left.label)
        assertEquals(270f, left.angle)

        assertEquals("Phải", right.label)
        assertEquals(90f, right.angle)
    }

    @Test
    fun `test all 10 authentic costume types exist and encompass all regions and styles`() {
        val costumes = CostumeDataRepository.costumes
        assertEquals(10, costumes.size)

        // Check new costumes exist
        assertNotNull(costumes.find { it.id == "ao_ba_ba" })
        assertNotNull(costumes.find { it.id == "ao_dai_cach_tan" })
        assertNotNull(costumes.find { it.id == "vien_linh" })
        assertNotNull(costumes.find { it.id == "long_bao" })

        // Check region coverage includes Nam Bo
        val namBoCostumes = costumes.filter { it.region == Region.NAM_BO }
        assertEquals(1, namBoCostumes.size)
        assertEquals("ao_ba_ba", namBoCostumes.first().id)

        // Check modern innovation style coverage
        val modernCostumes = costumes.filter { it.style == CostumeStyle.MODERN_INNOVATION }
        assertEquals(1, modernCostumes.size)
        assertEquals("ao_dai_cach_tan", modernCostumes.first().id)

        // Check contemporary dynasty coverage
        val contemporaryCostumes = costumes.filter { it.dynasty == Dynasty.CONTEMPORARY }
        assertEquals(2, contemporaryCostumes.size)
    }

    @Test
    fun `test ao ba ba compatibility with khan ran and mismatch with khan vanh`() {
        val aoBaBa = CostumeDataRepository.costumes.first { it.id == "ao_ba_ba" }
        val khanRan = CostumeDataRepository.accessories.first { it.id == "acc_khan_ran" }
        val khanVanh = CostumeDataRepository.accessories.first { it.id == "acc_khan_vanh" }

        val matchResult = CostumeDataRepository.checkCompatibility(aoBaBa, listOf(khanRan))
        assertEquals(CompatibilityGrade.PERFECT_HARMONY, matchResult.grade)

        val mismatchResult = CostumeDataRepository.checkCompatibility(aoBaBa, listOf(khanVanh))
        assertEquals(CompatibilityGrade.HISTORICAL_MISMATCH, mismatchResult.grade)
    }

    @Test
    fun `test long bao compatibility with dai ngoc and mismatch with folk headwear`() {
        val longBao = CostumeDataRepository.costumes.first { it.id == "long_bao" }
        val daiNgoc = CostumeDataRepository.accessories.first { it.id == "acc_dai_ngoc" }
        val nonBaTam = CostumeDataRepository.accessories.first { it.id == "acc_non_ba_tam" }

        val matchResult = CostumeDataRepository.checkCompatibility(longBao, listOf(daiNgoc))
        assertEquals(CompatibilityGrade.PERFECT_HARMONY, matchResult.grade)

        val mismatchResult = CostumeDataRepository.checkCompatibility(longBao, listOf(nonBaTam))
        assertEquals(CompatibilityGrade.HISTORICAL_MISMATCH, mismatchResult.grade)
    }

    @Test
    fun `test heritage events and weather recommendations`() {
        val events = CostumeDataRepository.events
        assertEquals(6, events.size)

        val weddingEvent = events.first { it.id == "wedding" }
        assertTrue(weddingEvent.suitableCostumeIds.contains("nhat_binh"))
        assertTrue(weddingEvent.suitableCostumeIds.contains("ao_tac"))

        val weathers = CostumeDataRepository.weathers
        assertEquals(4, weathers.size)

        val winterWeather = weathers.first { it.id == "chilly_winter" }
        assertTrue(winterWeather.suitableCostumeIds.contains("ao_tac"))
    }

    @Test
    fun `test view model select event and weather`() {
        val viewModel = CostumeViewModel()
        val folkEvent = CostumeDataRepository.events.first { it.id == "folk_festival" }
        viewModel.selectEvent(folkEvent)
        assertEquals("folk_festival", viewModel.uiState.value.selectedEvent.id)

        val summerWeather = CostumeDataRepository.weathers.first { it.id == "hot_summer" }
        viewModel.selectWeather(summerWeather)
        assertEquals("hot_summer", viewModel.uiState.value.selectedWeather.id)
    }

    @Test
    fun `test costume card bitmap generation creates valid front and back image`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val nhatBinh = CostumeDataRepository.costumes.first { it.id == "nhat_binh" }
        val outfit = SavedOutfit(
            id = "test_outfit",
            name = "Test Nhật Bình",
            costume = nhatBinh,
            selectedColor = CostumeDataRepository.colors.first(),
            selectedPattern = CostumeDataRepository.patterns.first(),
            selectedAccessories = emptyList(),
            compatibility = CostumeDataRepository.checkCompatibility(nhatBinh, emptyList())
        )
        val bitmap = com.example.cophuc.ui.components.CostumeCardBitmapGenerator.generateCardBitmap(context, outfit)
        assertNotNull(bitmap)
        assertEquals(1080, bitmap.width)
        assertEquals(1480, bitmap.height)
    }
}
