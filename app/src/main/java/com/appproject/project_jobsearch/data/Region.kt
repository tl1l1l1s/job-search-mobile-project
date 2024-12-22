package com.appproject.project_jobsearch.data

data class RegionData(
    val id: String?,
    val name: String,
)

class Region {
    companion object {
        val regionList: List<RegionData> = listOf(
            RegionData(null, "전체"),
            RegionData("101000", "서울"),
            RegionData("102000", "경기"),
            RegionData("103000", "광주"),
            RegionData("104000", "대구"),
            RegionData("105000", "대전"),
            RegionData("106000", "부산"),
            RegionData("107000", "울산"),
            RegionData("108000", "인천"),
            RegionData("109000", "강원"),
            RegionData("110000", "경남"),
            RegionData("111000", "경북"),
            RegionData("112000", "전남"),
            RegionData("113000", "전북"),
            RegionData("114000", "충북"),
            RegionData("115000", "충남"),
            RegionData("116000", "제주"),
            RegionData("117000", "전국"),
            RegionData("118000", "세종"),
            RegionData("210000", "아시아·중동"),
            RegionData("220000", "북·중미"),
            RegionData("230000", "남미"),
            RegionData("240000", "유럽"),
            RegionData("250000", "오세아니아"),
            RegionData("260000", "아프리카"),
            RegionData("270000", "남극대륙"),
            RegionData("280000", "기타해외")

        )
    }
}
