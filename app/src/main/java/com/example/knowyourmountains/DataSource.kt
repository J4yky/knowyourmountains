package com.example.knowyourmountains

object DataSource {

    fun getQuestions(): List<Question> {
        return listOf(
            Question(R.drawable.giewont, "Giewont", "Polska"),
            Question(R.drawable.rysy, "Rysy", "Polska"),
            Question(R.drawable.gerlachovskystit, "Gerlachovsky Stit", "Słowacja"),
            Question(R.drawable.koziwierch, "Kozi Wierch", "Polska"),
            Question(R.drawable.baranierohy, "Baranie Rohy", "Słowacja"),
            Question(R.drawable.kezmarskistit, "Kezmarski Stit", "Słowacja"),
            Question(R.drawable.koncista, "Koncista", "Słowacja"),
            Question(R.drawable.koscielec, "Kościelec", "Polska"),
            Question(R.drawable.krivan, "Krivan", "Słowacja"),
            Question(R.drawable.lomnickystit, "Lomnicky Stit", "Słowacja"),
            Question(R.drawable.maloloczniak, "Małołączniak", "Polska"),
            Question(R.drawable.mieguszowieckiszczytwielki, "Mięguszowiecki Szczyt Wielki", "Polska"),
            Question(R.drawable.mnich, "Mnich", "Polska"),
            Question(R.drawable.satan, "Satan", "Słowacja"),
            Question(R.drawable.skrajnygranat, "Skrajny Granat", "Polska"),
            Question(R.drawable.slavkovskistit, "Slavkovsky Stit", "Słowacja"),
            Question(R.drawable.swinica, "Świnica", "Polska"),
            Question(R.drawable.szpiglasowywierch, "Szpiglasowy Wierch", "Polska") ,
            Question(R.drawable.velkesolisko, "Velke Solisko", "Słowacja"),
            Question(R.drawable.vychodnavysoka, "Vychodna Vysoka", "Słowacja")
        )
    }
}