package com.example.cophuc.data

import androidx.compose.ui.graphics.Color
import com.example.cophuc.data.model.*

object CostumeDataRepository {

    // Palettes
    val colors = listOf(
        CostumeColor(
            id = "c_red",
            name = "Đỏ Son Hoàng Triều",
            hexValue = 0xFF941824,
            color = Color(0xFF941824),
            culturalMeaning = "Màu của hỷ khánh, tôn nghiêm và quyền quý trong nghi lễ cung đình."
        ),
        CostumeColor(
            id = "c_gold",
            name = "Vàng Hoàng Kim",
            hexValue = 0xFFD4A017,
            color = Color(0xFFD4A017),
            culturalMeaning = "Sắc màu tối thượng của bậc đế vương và hoàng tộc triều Nguyễn."
        ),
        CostumeColor(
            id = "c_purple",
            name = "Tím Cố Đô",
            hexValue = 0xFF6C3483,
            color = Color(0xFF6C3483),
            culturalMeaning = "Nét trầm mặc, kín đáo gắn liền với thiếu nữ và văn hóa xứ Huế."
        ),
        CostumeColor(
            id = "c_indigo",
            name = "Xanh Chàm Lam",
            hexValue = 0xFF1E3A5F,
            color = Color(0xFF1E3A5F),
            culturalMeaning = "Màu nhuộm tự nhiên từ củ chàm, tiêu biểu cho vẻ đẹp mộc mạc dân gian."
        ),
        CostumeColor(
            id = "c_jade",
            name = "Xanh Ngọc Bích",
            hexValue = 0xFF1B6B52,
            color = Color(0xFF1B6B52),
            culturalMeaning = "Biểu trưng cho sự thanh nhã, tài lộc và cốt cách cao quý."
        ),
        CostumeColor(
            id = "c_ivory",
            name = "Trắng Tơ Tằm",
            hexValue = 0xFFFAF4E8,
            color = Color(0xFFFAF4E8),
            culturalMeaning = "Chất liệu lụa thô tự nhiên, thuần khiết và thanh tân thuở xưa."
        )
    )

    // Patterns
    val patterns = listOf(
        CostumePattern(
            id = "p_crane",
            name = "Hạc Bay Thọ Triều",
            dynasty = Dynasty.NGUYEN,
            description = "Hình tượng chim hạc vờn mây, biểu trưng cho phẩm hạnh thanh cao và ước vọng trường thọ của vương triều.",
            symbolism = "Trường thọ, chính trực và sự tôn kính tổ tiên."
        ),
        CostumePattern(
            id = "p_dongson",
            name = "Trống Đồng Đông Sơn",
            dynasty = Dynasty.LY_TRAN,
            description = "Hoa văn hình học và chim Lạc cổ đại, thể hiện niềm tự hào nguồn cội ngàn năm dựng nước.",
            symbolism = "Hào khí sông núi, sinh khí đất trời và nguồn cội dân tộc."
        ),
        CostumePattern(
            id = "p_lotus",
            name = "Hoa Sen Nhã Nhạc",
            dynasty = Dynasty.LE,
            description = "Đóa sen nở rộ thuần khiết 'gần bùn mà chẳng hôi tanh mùi bùn', gắn bó sâu sắc trong mỹ thuật Phật giáo thời Lý - Trần - Lê.",
            symbolism = "Sự thanh bạch, giác ngộ và tấm lòng kiên định."
        ),
        CostumePattern(
            id = "p_cloud",
            name = "Mây Ngũ Sắc Cát Tường",
            dynasty = Dynasty.NGUYEN,
            description = "Dải mây lành ngũ sắc cuộn mềm mại, mang ý niệm thiên nhân tương ứng, mưa thuận gió hòa.",
            symbolism = "May mắn, điềm lành và sự bình an thịnh vượng."
        ),
        CostumePattern(
            id = "p_phoenix",
            name = "Phượng Hoàng Cung Đình",
            dynasty = Dynasty.NGUYEN,
            description = "Biểu tượng mẫu nghi thiên hạ, hoa văn dành riêng cho Hoàng hậu và các phi tần chốn hoàng cung.",
            symbolism = "Đức hạnh mẫu mực, sắc đẹp quý phái và phúc lộc viên mãn."
        )
    )

    // Accessories
    val accessories = listOf(
        AccessoryItem(
            id = "acc_khan_vanh",
            name = "Khăn Vành Dây",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.ROYAL),
            culturalDescription = "Khăn xếp quấn nhiều vòng từ dải lụa dài, dành riêng cho hoàng gia và mệnh phụ triều Nguyễn.",
            historicalSignificance = "Quy định phẩm phục nghiêm cẩn của triều Nguyễn, thể hiện thứ bậc tôn nghiêm chốn hoàng cung."
        ),
        AccessoryItem(
            id = "acc_non_ba_tam",
            name = "Nón Ba Tầm Quai Thao",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.LE, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.FOLK),
            culturalDescription = "Nón lá tròn phẳng vành rộng kết hợp dải quai thao đan tơ mềm mại, biểu tượng thiếu nữ Kinh Bắc.",
            historicalSignificance = "Gắn liền với các làn điệu Dân ca Quan họ và phong thái đằm thắm của phụ nữ Bắc Bộ cổ xưa."
        ),
        AccessoryItem(
            id = "acc_khan_mo_qua",
            name = "Khăn Mỏ Quạ",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.LE, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.FOLK),
            culturalDescription = "Khăn vuông the đen gấp chéo góc nhọn che trán, tôn nét mặt thanh tú đoan trang của cô gái Bắc Bộ.",
            historicalSignificance = "Nét văn hóa dân gian đồng bằng sông Hồng, đi kèm hàm răng đen nhánh và áo Tứ Thân."
        ),
        AccessoryItem(
            id = "acc_kieng_vang",
            name = "Kiềng Vàng Chạm Sen",
            category = "Trang sức cổ",
            suitableDynasties = listOf(Dynasty.NGUYEN, Dynasty.LE, Dynasty.CONTEMPORARY),
            suitableStyles = listOf(CostumeStyle.ROYAL, CostumeStyle.FOLK, CostumeStyle.MODERN_INNOVATION),
            culturalDescription = "Vòng cổ bằng vàng hoặc bạc uốn tròn chạm trổ tinh xảo, phụ kiện trang trọng ngày vu quy và lễ hội.",
            historicalSignificance = "Vật báu gia truyền của người phụ nữ Việt Nam qua nhiều thế hệ, tượng trưng cho hạnh phúc vẹn tròn."
        ),
        AccessoryItem(
            id = "acc_quat_tram",
            name = "Quạt Trầm Cung Đình",
            category = "Phụ kiện cầm tay",
            suitableDynasties = listOf(Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.ROYAL),
            culturalDescription = "Quạt xếp làm từ gỗ trầm hương quý, nan chạm trổ rồng phụng hoặc tranh thủy mặc trang nhã.",
            historicalSignificance = "Đồ dùng nghi lễ và phong thái đoan trang của các tiểu thư khuê các chốn đế đô Huế."
        ),
        AccessoryItem(
            id = "acc_hai_theu",
            name = "Hài Thêu Mũi Cong",
            category = "Hài dép",
            suitableDynasties = listOf(Dynasty.NGUYEN, Dynasty.LE),
            suitableStyles = listOf(CostumeStyle.ROYAL, CostumeStyle.FOLK),
            culturalDescription = "Đôi hài bằng nhung lụa có mũi uốn cong duyên dáng, thêu kim tuyến hoa sen hoặc mây lành tỉ mỉ.",
            historicalSignificance = "Trang phục cung đình và gia đình khá giả thời Nguyễn, bước đi uyển chuyển nhẹ nhàng."
        ),
        AccessoryItem(
            id = "acc_non_la",
            name = "Nón Lá Xứ Huế",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.NGUYEN, Dynasty.CONTEMPORARY),
            suitableStyles = listOf(CostumeStyle.ROYAL, CostumeStyle.FOLK, CostumeStyle.MODERN_INNOVATION),
            culturalDescription = "Chiếc nón chóp đan từ lá gồi thanh mỏng, có thể ẩn hiện bài thơ khi soi bóng dưới nắng vàng.",
            historicalSignificance = "Vật dụng che mưa nắng trở thành linh hồn thi ca của phụ nữ Việt Nam khắp ba miền."
        ),
        AccessoryItem(
            id = "acc_ngoc_boi",
            name = "Bội Ngọc Phong Thủy",
            category = "Trang sức cổ",
            suitableDynasties = listOf(Dynasty.LY_TRAN, Dynasty.LE, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.ROYAL),
            culturalDescription = "Miếng ngọc bích chạm hình mây rồng đeo bên hông thắt lưng, phát tiếng lanh lảnh thanh khiết khi bước đi.",
            historicalSignificance = "Biểu trưng của quân tử và quý tộc xưa, nhắc nhở dáng đi đĩnh đạc và tâm hồn thuần tịnh."
        ),
        AccessoryItem(
            id = "acc_khan_ran",
            name = "Khăn Rằn Nam Bộ",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.CONTEMPORARY, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.FOLK, CostumeStyle.MODERN_INNOVATION),
            culturalDescription = "Khăn dệt sợi bông kẻ ô caro mộc mạc vắt vai hoặc quấn đầu, biểu tượng người dân Nam Bộ.",
            historicalSignificance = "Gắn liền với công cuộc khai hoang mở cõi phương Nam, biểu trưng cho đức tính chịu thương chịu khó."
        ),
        AccessoryItem(
            id = "acc_dai_ngoc",
            name = "Đai Ngọc Hoàng Gia",
            category = "Trang sức cổ",
            suitableDynasties = listOf(Dynasty.LY_TRAN, Dynasty.LE, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.ROYAL),
            culturalDescription = "Vòng đai bọc gấm then cài các bản ngọc bích chạm rồng mây tinh xảo, phụ kiện đại triều phục.",
            historicalSignificance = "Biểu tượng quyền uy tối thượng trong quy chế phẩm phục triều nghi Đại Việt, định rõ đẳng cấp tôn ty."
        ),
        AccessoryItem(
            id = "acc_tram_cai",
            name = "Trâm Cài Phượng Kim Tuyến",
            category = "Trang sức đầu",
            suitableDynasties = listOf(Dynasty.LY_TRAN, Dynasty.LE, Dynasty.NGUYEN),
            suitableStyles = listOf(CostumeStyle.ROYAL),
            culturalDescription = "Cây trâm mạ vàng chạm phượng hoàng ngậm châu, rủ chuỗi ngọc lấp lánh cài trên búi tóc tôn quý.",
            historicalSignificance = "Vật báu trang sức cung đình của hoàng hậu và mệnh phụ, tượng trưng cho phúc lộc và phẩm hạnh đoan trang."
        )
    )

    // Costumes
    val costumes = listOf(
        CostumeItem(
            id = "nhat_binh",
            name = "Áo Nhật Bình",
            category = "Áo Lễ Cung Đình",
            dynasty = Dynasty.NGUYEN,
            region = Region.TRUNG_BO,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.WEDDING, Occasion.TET_HOLIDAY, Occasion.ART_PHOTOSHOOT),
            availableColors = colors.filter { it.id in listOf("c_red", "c_gold", "c_purple", "c_jade") },
            availablePatterns = patterns.filter { it.id in listOf("p_phoenix", "p_crane", "p_cloud") },
            compatibleAccessoryIds = listOf("acc_khan_vanh", "acc_kieng_vang", "acc_quat_tram", "acc_hai_theu", "acc_ngoc_boi"),
            culturalDescription = "Áo đối khâm cổ hình chữ nhật đặc trưng triều Nguyễn, viền dải ngũ hành rực rỡ, trang phục thường triều của phi tần và áo cưới quý tộc.",
            detailedHistory = "Áo Nhật Bình có nguồn gốc từ áo Phi Phong thời Minh, được cải biến đỉnh cao dưới thời vua Gia Long và Minh Mạng triều Nguyễn. Cổ áo khi cài lại tạo thành hình chữ nhật trước ngực ('Nhật'). Dải viền cổ được thêu hoa văn ngũ sắc tượng trưng cho ngũ hành.",
            wearingNorms = "Chuẩn mực cung đình bắt buộc phối cùng Khăn Vành Dây, mặc kèm áo lót trắng bên trong và quần thụng lụa trắng. Khi làm lễ mặc cùng hài thêu."
        ),
        CostumeItem(
            id = "tu_than",
            name = "Áo Tứ Thân",
            category = "Áo Dân Gian Truyền Thống",
            dynasty = Dynasty.LE,
            region = Region.BAC_BO,
            style = CostumeStyle.FOLK,
            occasions = listOf(Occasion.FESTIVAL, Occasion.ART_PHOTOSHOOT, Occasion.TET_HOLIDAY),
            availableColors = colors.filter { it.id in listOf("c_indigo", "c_ivory", "c_red") },
            availablePatterns = patterns.filter { it.id in listOf("p_lotus", "p_dongson") },
            compatibleAccessoryIds = listOf("acc_non_ba_tam", "acc_khan_mo_qua", "acc_kieng_vang"),
            culturalDescription = "Gồm bốn vạt tượng trưng cho tứ thân phụ mẫu, hai vạt trước buộc eo duyên dáng, biểu tượng sâu sắc của người phụ nữ nông thôn Bắc Bộ.",
            detailedHistory = "Xuất hiện từ thế kỷ XVII - XVIII và phổ biến khắp đồng bằng Bắc Bộ. Bốn vạt áo tượng trưng cho cha mẹ đẻ và cha mẹ chồng, yếm đỏ thắm bên trong như lời tâm tình e ấp của người con gái.",
            wearingNorms = "Thường mặc phối cùng Áo Yếm lụa hồng/đào, thắt lưng xanh hoa lý, nón quai thao (nón Ba Tầm) và khăn mỏ quạ."
        ),
        CostumeItem(
            id = "ao_tac",
            name = "Áo Tấc (Áo Tay Thụm)",
            category = "Lễ Phục Trang Trọng",
            dynasty = Dynasty.NGUYEN,
            region = Region.ALL,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.WEDDING, Occasion.TET_HOLIDAY, Occasion.FESTIVAL),
            availableColors = colors,
            availablePatterns = patterns.filter { it.id in listOf("p_crane", "p_cloud", "p_lotus") },
            compatibleAccessoryIds = listOf("acc_khan_vanh", "acc_kieng_vang", "acc_quat_tram", "acc_hai_theu"),
            culturalDescription = "Lễ phục ngũ thân tay rộng dài thụng hơn một tấc, dùng cho cả nam lẫn nữ trong các dịp đại lễ, cúng tế và cưới hỏi thời Nguyễn.",
            detailedHistory = "Là một thể thức của áo ngũ thân nhưng phần tay áo được may rộng thụng buông dài. Dáng áo toát lên sự đĩnh đạc, trang nghiêm, tượng trưng cho nền nếp gia phong của người xưa.",
            wearingNorms = "Đi cùng khăn đóng hoặc khăn vành dây, hai tay thường chắp trước bụng để lộ ống tay buông rủ thanh nhã."
        ),
        CostumeItem(
            id = "ngu_than_tay_chen",
            name = "Áo Dài Ngũ Thân Tay Chẽn",
            category = "Thường Phục Đô Thị",
            dynasty = Dynasty.NGUYEN,
            region = Region.ALL,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.TET_HOLIDAY, Occasion.ART_PHOTOSHOOT, Occasion.WEDDING),
            availableColors = colors,
            availablePatterns = patterns,
            compatibleAccessoryIds = listOf("acc_khan_vanh", "acc_non_la", "acc_kieng_vang", "acc_quat_tram", "acc_hai_theu"),
            culturalDescription = "Năm thân áo may khép kín tượng trưng đạo ngũ thường (Nhân-Lễ-Nghĩa-Trí-Tín), tay chẽn gọn gàng, tiền thân của áo dài Việt Nam hiện đại.",
            detailedHistory = "Được Chúa Nguyễn Phúc Khoát định hình năm 1744 và vua Minh Mạng hoàn thiện quy chuẩn toàn quốc năm 1827-1837 để thống nhất văn hóa y phục Đại Nam.",
            wearingNorms = "Có thể kết hợp khăn đóng, nón lá, kiềng bạc hoặc quạt xếp tơ lụa trong đời sống thường nhật."
        ),
        CostumeItem(
            id = "giao_linh",
            name = "Áo Giao Lĩnh",
            category = "Cổ Phục Thượng Cổ",
            dynasty = Dynasty.LY_TRAN,
            region = Region.BAC_BO,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.FESTIVAL, Occasion.ART_PHOTOSHOOT),
            availableColors = colors.filter { it.id in listOf("c_indigo", "c_ivory", "c_red", "c_jade") },
            availablePatterns = patterns.filter { it.id in listOf("p_dongson", "p_lotus") },
            compatibleAccessoryIds = listOf("acc_ngoc_boi", "acc_kieng_vang", "acc_hai_theu"),
            culturalDescription = "Dáng áo cổ chéo vạt đè lên nhau phổ biến thời Lý - Trần - Hậu Lê, mang đậm phong khí hào sảng ngàn năm văn hiến phương Nam.",
            detailedHistory = "Áo Giao Lĩnh là dạng trang phục cổ xưa nhất của người Việt, từng ghi nhận trên các tượng đá và thư tịch cổ triều Lý - Trần. Thân áo dài rộng, cổ vắt chéo sang bên phải.",
            wearingNorms = "Phối cùng đai lưng bản lớn, bội ngọc và búi tóc cài trâm."
        ),
        CostumeItem(
            id = "doi_kham",
            name = "Áo Đối Khâm Quý Tộc",
            category = "Lễ Phục Quý Tộc",
            dynasty = Dynasty.LE,
            region = Region.BAC_BO,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.ART_PHOTOSHOOT, Occasion.FESTIVAL),
            availableColors = colors.filter { it.id in listOf("c_gold", "c_red", "c_purple") },
            availablePatterns = patterns.filter { it.id in listOf("p_lotus", "p_phoenix", "p_cloud") },
            compatibleAccessoryIds = listOf("acc_kieng_vang", "acc_ngoc_boi", "acc_hai_theu", "acc_quat_tram"),
            culturalDescription = "Áo có hai vạt đối xứng buông thẳng song song, thường mặc khoác ngoài cùng tạo cảm giác thướt tha, quý phái thời Lê Sơ.",
            detailedHistory = "Được tầng lớp quý tộc thời Hậu Lê ưa chuộng trong các dịp dạ yến hoặc lễ hội cung đình. Tà áo thả lỏng để lộ lớp áo lót và yếm thêu hoa văn tinh xảo bên trong.",
            wearingNorms = "Thích hợp phối với trang sức cổ ngọc bội và trâm cài tóc mạ vàng."
        ),
        CostumeItem(
            id = "ao_ba_ba",
            name = "Áo Bà Ba Nam Bộ",
            category = "Dân Gian Nam Bộ",
            dynasty = Dynasty.CONTEMPORARY,
            region = Region.NAM_BO,
            style = CostumeStyle.FOLK,
            occasions = listOf(Occasion.FESTIVAL, Occasion.ART_PHOTOSHOOT, Occasion.TET_HOLIDAY),
            availableColors = colors.filter { it.id in listOf("c_indigo", "c_jade", "c_ivory", "c_purple", "c_gold") },
            availablePatterns = patterns.filter { it.id in listOf("p_lotus", "p_cloud") },
            compatibleAccessoryIds = listOf("acc_khan_ran", "acc_non_la", "acc_kieng_vang"),
            culturalDescription = "Thân áo ngắn xẻ tà hông, cổ tròn xẻ ngực cài nút khuy, hai túi trước tiện lợi, nét duyên đôn hậu của miền sông nước Nam Bộ.",
            detailedHistory = "Xuất hiện vào thế kỷ XIX ở Nam Bộ, dung hợp giữa nét mộc mạc và khí hậu phương Nam sông nước. Áo may ôm vừa vặn, hai vạt ngắn xẻ tà hông, mặc cùng quần lụa đen bóng, khăn rằn và nón lá chóp.",
            wearingNorms = "Mặc cùng quần lụa đen bóng hoặc trắng, quàng khăn rằn Nam Bộ kẻ caro quanh cổ hoặc vắt vai, đội nón lá chóp thanh tao."
        ),
        CostumeItem(
            id = "ao_dai_cach_tan",
            name = "Áo Dài Cách Tân Lemur",
            category = "Tân Thời Kiêu Sa",
            dynasty = Dynasty.CONTEMPORARY,
            region = Region.ALL,
            style = CostumeStyle.MODERN_INNOVATION,
            occasions = listOf(Occasion.TET_HOLIDAY, Occasion.WEDDING, Occasion.ART_PHOTOSHOOT, Occasion.FESTIVAL),
            availableColors = colors,
            availablePatterns = patterns,
            compatibleAccessoryIds = listOf("acc_khan_vanh", "acc_kieng_vang", "acc_non_la", "acc_quat_tram", "acc_hai_theu", "acc_tram_cai"),
            culturalDescription = "Biến tấu tân thời khởi xướng từ phong trào Lemur thập niên 1930, kết hợp phom dáng truyền thống với thẩm mỹ hiện đại.",
            detailedHistory = "Lấy cảm hứng từ cuộc cách tân y phục của họa sĩ Cát Tường (Lemur) năm 1934 tại Hà Nội. Áo dài được chiết eo mềm mại, mở ra kỷ nguyên rực rỡ của tà áo dài Việt Nam đương đại.",
            wearingNorms = "Phối cùng quần lụa ống rộng hoặc chân váy xòe, điểm xuyết kiềng vàng hoặc quạt trầm quý phái."
        ),
        CostumeItem(
            id = "vien_linh",
            name = "Áo Viên Lĩnh (Đoàn Lĩnh)",
            category = "Đại Triều Phục Thượng Cổ",
            dynasty = Dynasty.LY_TRAN,
            region = Region.BAC_BO,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.FESTIVAL, Occasion.ART_PHOTOSHOOT),
            availableColors = colors.filter { it.id in listOf("c_red", "c_gold", "c_purple", "c_indigo", "c_jade") },
            availablePatterns = patterns.filter { it.id in listOf("p_dongson", "p_lotus", "p_crane", "p_cloud") },
            compatibleAccessoryIds = listOf("acc_dai_ngoc", "acc_ngoc_boi", "acc_hai_theu", "acc_quat_tram", "acc_tram_cai"),
            culturalDescription = "Áo cổ tròn khép kín (đoàn lĩnh), cài khuy lệch vai phải, tay thụng rộng uy nghiêm, bổ tử thêu trước ngực thời Lý - Trần - Lê.",
            detailedHistory = "Viên Lĩnh là dạng lễ phục cung đình và quan chế phổ biến bậc nhất suốt từ thời nhà Lý đến thời Hậu Lê. Cổ áo tròn ôm lấy chân cổ, thân áo thụng rộng, bổ tử thêu chim muông hoặc thú linh theo phẩm hàm.",
            wearingNorms = "Bắt buộc thắt Đai Ngọc bọc vàng quanh eo, đeo Bội Ngọc bên hông, đi hài thêu ngự tỷ và búi tóc cài trâm."
        ),
        CostumeItem(
            id = "long_bao",
            name = "Long Bào Hoàng Đế Triều Nguyễn",
            category = "Hoàng Bào Tối Thượng",
            dynasty = Dynasty.NGUYEN,
            region = Region.TRUNG_BO,
            style = CostumeStyle.ROYAL,
            occasions = listOf(Occasion.FESTIVAL, Occasion.ART_PHOTOSHOOT),
            availableColors = colors.filter { it.id in listOf("c_gold", "c_red") },
            availablePatterns = patterns.filter { it.id in listOf("p_cloud", "p_phoenix", "p_crane") },
            compatibleAccessoryIds = listOf("acc_dai_ngoc", "acc_ngoc_boi", "acc_hai_theu", "acc_quat_tram", "acc_khan_vanh", "acc_tram_cai"),
            culturalDescription = "Phẩm phục tôn nghiêm tối thượng của bậc Hoàng đế, thêu đại long ngũ trảo cuộn mây lửa, đồ án Thủy Ba Tam Sơn lộng lẫy uy nghiêm.",
            detailedHistory = "Long Bào triều Nguyễn được chế tác công phu bằng tơ vàng chính hoàng, thêu đại long ngũ trảo bằng chỉ vàng kim tuyến rực rỡ, chân vạt thêu sóng biển cuộn trào tượng trưng cho sơn hà xã tắc thái bình thịnh trị.",
            wearingNorms = "Mặc cùng đai ngọc hoàng tộc, hài ngự thêu kim tuyến rồng phụng, dùng trong các nghi lễ đăng quang, tế Giao và thiết đại triều."
        )
    )

    // Validation engine: Checks compatibility between costume & accessories
    fun checkCompatibility(costume: CostumeItem, selectedAccessories: List<AccessoryItem>): CompatibilityCheckResult {
        if (selectedAccessories.isEmpty()) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.PERFECT_HARMONY,
                title = "Bộ trang phục nguyên bản",
                message = "Bạn đang ngắm nhìn dáng áo ${costume.name} thuần khiết. Hãy thêm phụ kiện để hoàn thiện diện mạo phong cách!",
                recommendations = "Gợi ý: Thêm phụ kiện tiêu biểu như ${accessories.find { it.id in costume.compatibleAccessoryIds }?.name ?: "Trang sức truyền thống"}."
            )
        }

        val incompatibleItems = selectedAccessories.filter { it.id !in costume.compatibleAccessoryIds }

        // Specific high-profile cultural mismatches:
        // E.g. Áo Nhật Bình (Cung đình Huế triều Nguyễn) worn with Nón Ba Tầm (Dân gian Kinh Bắc)
        val hasNhatBinhAndNonBaTam = costume.id == "nhat_binh" && selectedAccessories.any { it.id == "acc_non_ba_tam" }
        if (hasNhatBinhAndNonBaTam) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.HISTORICAL_MISMATCH,
                title = "Lệch bối cảnh cung đình & dân gian",
                message = "Áo Nhật Bình là phẩm phục quý tộc triều Nguyễn xứ Huế, còn Nón Ba Tầm mang phong thái dân gian Bắc Bộ. Hai vật phẩm này không đi cùng nhau trong lịch sử.",
                recommendations = "Nên đổi sang Khăn Vành Dây hoặc Khăn Đóng để tôn vinh trọn vẹn nét đài các hoàng gia triều Nguyễn!"
            )
        }

        // Áo Tứ Thân (Dân tộc Kinh Bắc) worn with Khăn Vành Dây (Cung đình triều Nguyễn)
        val hasTuThanAndKhanVanh = costume.id == "tu_than" && selectedAccessories.any { it.id == "acc_khan_vanh" }
        if (hasTuThanAndKhanVanh) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.HISTORICAL_MISMATCH,
                title = "Lệch quy chuẩn phẩm phục",
                message = "Khăn Vành Dây là quy chế nghiêm cẩn dành cho phụ nữ hoàng cung triều Nguyễn, không phù hợp khi đi cùng dáng áo Tứ Thân mộc mạc dân gian.",
                recommendations = "Hãy chọn Khăn Mỏ Quạ hoặc Nón Ba Tầm Quai Thao để chuẩn nét duyên Quan họ Kinh Bắc."
            )
        }

        // Áo Bà Ba (Dân gian Nam Bộ) worn with Khăn Vành Dây (Hoàng cung triều Nguyễn)
        val hasAoBaBaAndKhanVanh = costume.id == "ao_ba_ba" && selectedAccessories.any { it.id == "acc_khan_vanh" }
        if (hasAoBaBaAndKhanVanh) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.HISTORICAL_MISMATCH,
                title = "Lệch phong thái dân dã & cung đình",
                message = "Áo Bà Ba là trang phục mộc mạc lao động miền sông nước Nam Bộ, không phù hợp khi kết hợp cùng Khăn Vành Dây quý tộc hoàng gia triều Nguyễn.",
                recommendations = "Nên đổi sang Khăn Rằn Nam Bộ hoặc Nón Lá để tôn trọn vẻ đẹp đôn hậu thuần hậu miền Tây."
            )
        }

        // Long Bào (Hoàng đế tối thượng) worn with Nón Ba Tầm hoặc Khăn Mỏ Quạ (Dân gian)
        val hasLongBaoAndFolkHeadwear = costume.id == "long_bao" && selectedAccessories.any { it.id in listOf("acc_non_ba_tam", "acc_khan_mo_qua", "acc_khan_ran") }
        if (hasLongBaoAndFolkHeadwear) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.HISTORICAL_MISMATCH,
                title = "Lệch tôn ti đế vương & dân gian",
                message = "Long Bào là hoàng phục tối thượng của bậc Thiên tử, tuyệt đối không phối cùng các phụ kiện bình dân như nón Ba Tầm, khăn mỏ quạ hay khăn rằn.",
                recommendations = "Hãy phối cùng Đai Ngọc Hoàng Gia, Hài Thêu hoặc Khăn Vành Dây để giữ trọn tôn nghiêm hoàng đế."
            )
        }

        // Áo Viên Lĩnh (Triều phục thượng cổ) worn with Khăn Mỏ Quạ
        val hasVienLinhAndKhanMoQua = costume.id == "vien_linh" && selectedAccessories.any { it.id == "acc_khan_mo_qua" }
        if (hasVienLinhAndKhanMoQua) {
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.HISTORICAL_MISMATCH,
                title = "Lệch thời kỳ & quy chuẩn triều nghi",
                message = "Áo Viên Lĩnh là quan phục đại triều thời Lý - Trần - Lê, không đi cùng khăn mỏ quạ của thiếu nữ dân gian Bắc Bộ sau này.",
                recommendations = "Hãy chọn Đai Ngọc Hoàng Gia, Bội Ngọc Phong Thủy và Trâm Cài Phượng để chuẩn phong thái quý tộc."
            )
        }

        if (incompatibleItems.isNotEmpty()) {
            val itemNames = incompatibleItems.joinToString(", ") { it.name }
            return CompatibilityCheckResult(
                grade = CompatibilityGrade.CREATIVE_FUSION,
                title = "Phong cách giao thoa cách tân",
                message = "$itemNames mang đặc trưng triều đại hoặc phong cách khác biệt so với ${costume.name}. Đây là sự thử nghiệm phá cách hiện đại.",
                recommendations = "Để đạt chuẩn phục cổ lịch sử, ưu tiên các phụ kiện: ${costume.compatibleAccessoryIds.mapNotNull { id -> accessories.find { it.id == id }?.name }.take(3).joinToString(", ")}."
            )
        }

        // All accessories match!
        return CompatibilityCheckResult(
            grade = CompatibilityGrade.PERFECT_HARMONY,
            title = "Đại hòa hợp truyền thống chuẩn mực",
            message = "Tuyệt vời! Toàn bộ phụ kiện đã chọn đều tuân thủ chính xác thời kỳ ${costume.dynasty.title} và phong thái ${costume.style.title}.",
            recommendations = "Sự phối hợp hoàn hảo này làm sống dậy vẻ đẹp tinh tế, chuẩn mực của văn hóa phục sức Việt Nam."
        )
    }

    // Heritage Events (Sự kiện người dùng sẽ tham gia)
    val events = listOf(
        HeritageEvent(
            id = "wedding",
            title = "Hôn Lễ & Cưới Hỏi Truyền Thống",
            subtitle = "Lễ cưới, rước dâu, bái lạy gia tiên",
            suitableCostumeIds = listOf("nhat_binh", "ao_tac", "ngu_than_tay_chen"),
            tips = "Cô dâu thường mặc Áo Nhật Bình đỏ/vàng hoàng kim, chú rể mặc Áo Tấc hoặc Áo Ngũ Thân xanh chàm/đen lịch lãm.",
            culturalEtiquette = "Chuẩn mực lễ nghi trang nghiêm, cài cổ kín đáo, kết hợp khăn đóng hoặc khăn vành dây truyền thống."
        ),
        HeritageEvent(
            id = "royal_court",
            title = "Đại Triều & Nghi Lễ Hoàng Cung",
            subtitle = "Sự kiện cung đình, lễ hội tái hiện lịch sử",
            suitableCostumeIds = listOf("long_bao", "nhat_binh", "vien_linh", "giao_linh"),
            tips = "Long Bào cho bậc đế vương; Nhật Bình cho hoàng tộc nội cung; Viên Lĩnh / Áo Đoàn cho quan lại triều đình.",
            culturalEtiquette = "Tuyệt đối tuân thủ quy chế phẩm trật ngũ hành, hoa văn Long - Phượng và triều quan."
        ),
        HeritageEvent(
            id = "folk_festival",
            title = "Lễ Hội Dân Gian & Đình Làng",
            subtitle = "Hội xuân, trẩy hội chùa, hát quan họ truyền thống",
            suitableCostumeIds = listOf("tu_than", "ngu_than_tay_chen", "ao_ba_ba", "giao_linh"),
            tips = "Áo Tứ Thân thắt bao tượng rực rỡ; Áo Bà Ba mát mẻ hội xuân Nam Bộ; Áo Ngũ Thân thanh lịch nho nhã.",
            culturalEtiquette = "Sắc màu rộn rã đón xuân, kết hợp nón ba tầm, nón lá hoặc khăn rằn mộc mạc."
        ),
        HeritageEvent(
            id = "ancestral_rites",
            title = "Tế Lễ Gia Tộc & Cúng Giỗ",
            subtitle = "Lễ bái tiên tổ, tế tự trang nghiêm tại từ đường",
            suitableCostumeIds = listOf("ao_tac", "ngu_than_tay_chen", "doi_kham"),
            tips = "Áo Tấc tay thụng là lễ phục gia quy chuẩn mực nhất của người Việt, toát lên sự cung kính và nề nếp gia phong.",
            culturalEtiquette = "Nên chọn sắc màu nền nã (lam chàm, đen tuyền, xanh huyền), tránh màu sắc quá sặc sỡ."
        ),
        HeritageEvent(
            id = "cultural_photoshoot",
            title = "Chụp Ảnh Kỷ Yếu & Sự Kiện Cổ Trang",
            subtitle = "Lưu giữ thanh xuân, quảng bá văn hóa cổ phục",
            suitableCostumeIds = listOf("nhat_binh", "ao_tac", "ngu_than_tay_chen", "ao_dai_tan_thoi", "tu_than"),
            tips = "Tự do sáng tạo phối hoa văn cung đình và phụ kiện quạt trầm, trâm cài để tôn vẻ thanh tao quý phái.",
            culturalEtiquette = "Tôn trọng phom dáng cổ truyền chuẩn mực, giữ gìn nét đẹp đoan trang của y phục dân tộc."
        ),
        HeritageEvent(
            id = "casual_daily",
            title = "Thường Nhật & Dạo Phố Giao Lưu",
            subtitle = "Sinh hoạt hàng ngày, đàm đạo tao nhã",
            suitableCostumeIds = listOf("ngu_than_tay_chen", "ao_ba_ba", "ao_dai_tan_thoi"),
            tips = "Áo Ngũ Thân tay chẽn gọn gàng, may bằng vải lụa tơ tằm thoáng mát, vừa thanh lịch vừa thuận tiện vận động.",
            culturalEtiquette = "Phong thái giản dị, thanh tao, tôn vinh nét đẹp văn hóa trong nhịp sống đương đại."
        )
    )

    // Heritage Weather Conditions (Thời tiết & Khí hậu)
    val weathers = listOf(
        HeritageWeather(
            id = "sunny_warm",
            title = "Nắng Ấm / Tiết Xuân Hạ",
            tempRange = "24°C - 30°C",
            suitableCostumeIds = listOf("ngu_than_tay_chen", "tu_than", "ao_ba_ba", "ao_dai_tan_thoi", "doi_kham"),
            advice = "Chất vải tơ tằm mỏng nhẹ, sa cát, lụa Hà Đông thoáng mát; tay chẽn hoặc yếm lụa nhẹ nhàng."
        ),
        HeritageWeather(
            id = "cool_autumn",
            title = "Mát Mẻ / Tiết Trời Thu Dịu",
            tempRange = "20°C - 25°C",
            suitableCostumeIds = listOf("nhat_binh", "ao_tac", "ngu_than_tay_chen", "vien_linh", "giao_linh", "long_bao"),
            advice = "Thời tiết lý tưởng nhất cho mọi loại cổ phục nhiều lớp, gấm lụa thêu kim tuyến tinh xảo."
        ),
        HeritageWeather(
            id = "chilly_winter",
            title = "Se Lạnh / Tiết Đông Thu",
            tempRange = "14°C - 19°C",
            suitableCostumeIds = listOf("ao_tac", "nhat_binh", "long_bao", "vien_linh", "giao_linh"),
            advice = "Ưu tiên áo lót trong phối áo ngoài tay thụng nhiều lớp (Áo Tấc, Nhật Bình) chất liệu gấm dày dặn giữ ấm."
        ),
        HeritageWeather(
            id = "hot_summer",
            title = "Nắng Nóng / Mùa Hè Oi Ả",
            tempRange = "> 30°C",
            suitableCostumeIds = listOf("ao_ba_ba", "tu_than", "ngu_than_tay_chen"),
            advice = "Áo Bà Ba Nam Bộ vải lụa tơ mỏng nhẹ, hoặc Ngũ Thân tay chẽn chất liệu đũi xô hút mồ hôi tốt."
        )
    )
}
