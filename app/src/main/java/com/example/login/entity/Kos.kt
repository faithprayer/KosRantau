package com.example.login.entity

class Kos (val namaKos: String, val alamatKos : String, val namaPemilik : String) {

    companion object{
        @JvmField
        var listOfKos = arrayOf(
            Kos("Kos A", "Jl.Budi 1", "Andri"),
            Kos("Kos B", "Jl.Budi 2", "Dian"),
            Kos("Kos C", "Jl.Budi 3", "Dila"),
            Kos("Kos D", "Jl.Budi 4", "Rama"),
            Kos("Kos E", "Jl.Budi 5", "Vallent"),
            Kos("Kos F", "Jl.Budi 6", "Kadek"),
            Kos("Kos G", "Jl.Budi 7", "Satya"),
            Kos("Kos H", "Jl.Budi 8", "Gunda"),
            Kos("Kos I", "Jl.Budi 9", "Fajar"),
            Kos("Kos J", "Jl.Budi 10", "Bintang"),
            Kos("Kos K", "Jl.Budi 11", "Galih"),
            Kos("Kos L", "Jl.Budi 12", "Dewantara"),
            Kos("Kos M", "Jl.Budi 13", "Galih")
        )
    }
}