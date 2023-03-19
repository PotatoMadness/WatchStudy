package com.example.testevinfra.data

data class StationInfo (
    val id: String,
    val snm: String,
    val op: String,
    val lat: String,
    val lon: String,
    val cl: List<Charger>,
    val roof: String,
    val st: Int
    ){
    val totalType: String get() {
        return when (st) {
            CHARGER_TYPE_DCDEMO -> "DC 차데모"
            CHARGER_TYPE_DCDEMO_AC -> "DC 차데모 • AC 3상"
            CHARGER_TYPE_DCCOMBO -> "DC 콤보"
            CHARGER_TYPE_DCDEMO_DCCOMBO -> "DC 차데모 • DC 콤보"
            CHARGER_TYPE_DCDEMO_DCCOMBO_AC -> "DC 차데모 • DC 콤보 • AC 3상"
            CHARGER_TYPE_AC -> "AC 3상"
            CHARGER_TYPE_SUPER_CHARGER -> "수퍼차저"
            CHARGER_TYPE_DESTINATION -> "데스티네이션"
            else -> ""
        }
    }
    val lmStr: String get() {
        val hasLimit = cl.any {
            it.lm == "N" || it.lm == "UN"
        }
        return if (hasLimit) "비개방" else "개방"
    }

    val roofStr: String get() {
        return when(roof) {
            "0" -> "야외"
            "1" -> "실내"
            "2" -> "캐노피"
            else -> ""
        }
    }

    val standard:List<Charger> get() = cl.filter { it.p.toInt() < 50 }
    val standardActive:List<Charger> get() = cl.filter { it.p.toInt() < 50 && it.cst == "2"}
    val fast:List<Charger> get() =  cl.filter { it.p.toInt() >= 50 }
    val fastActive:List<Charger> get() =  cl.filter { it.p.toInt() >= 50 && it.cst == "2" }
}

data class Charger (
    val cid: String,
    val cst: String,
    val p: String,
    val tid: String,
    val rd: String?,
    val method: String,
    val s: String?,
    val lm: String
    ){
}
