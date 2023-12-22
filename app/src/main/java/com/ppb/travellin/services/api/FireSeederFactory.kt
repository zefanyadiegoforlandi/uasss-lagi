package com.ppb.travellin.services.api

import android.util.Log
import com.ppb.travellin.services.model.Stations
import com.ppb.travellin.services.model.Trains

class FireSeederFactory {
    val fireConsole = FireConsole()

    fun seedTrains(key: String = "") {
        if (key == "confirm") {
            val listOfTrains = listOf<Trains>(
                Trains(name="Argo Bromo Anggrek", weight=10),
                Trains(name="Argo Lawu", weight=9),
                Trains(name="Argo Dwipangga", weight=11),
                Trains(name="Argo Muria", weight=12),
                Trains(name="Argo Sindoro", weight=13),

                Trains(name="Argo Parahyangan", weight=20),
                Trains(name="Argo Wilis", weight=21),
                Trains(name="Argo Jati", weight=22),

                Trains(name="Prameks", weight=5),
                Trains(name="Kutojaya Utara", weight=6),
                Trains(name="Kutojaya Selatan", weight=7),
                Trains(name="Kutojaya Utara", weight=8),

                Trains(name="Kertajaya", weight=14),
                Trains(name="Logawa", weight=15),
                Trains(name="Jayabaya", weight=16),
                Trains(name="Gumarang", weight=5),
                Trains(name="Bima", weight=24),
                Trains(name="Bengawan", weight=7),
                Trains(name="Baturraden Express", weight=8),
                Trains(name="Sancaka", weight=9),
                Trains(name="Turangga", weight=10),
                Trains(name="Taksaka", weight=11),
            )

            listOfTrains.forEach {
                fireConsole.trainsRef.add(it).addOnSuccessListener { documentReference ->
                    it.id = documentReference.id
                    documentReference.set(it).addOnFailureListener {
                        println("Error adding train id : ${it.message}")
                    }
                }
            }
        }
    }

    fun seedStasion(key : String = "") {
        if (key == "confirm") {
            val listOfStation = listOf<Stations>(
                Stations(id="1", city="Jakarta", name="Stasiun Gambir", weight=1),
                Stations(id="2", city="Jakarta", name="Stasiun Pasar Senen", weight=2),
                Stations(id="3", city="Jakarta", name="Stasiun Jatinegara", weight=3),
                Stations(id="4", city="Jakarta", name="Stasiun Manggarai", weight=4),
                Stations(id="5", city="Jakarta", name="Stasiun Tanah Abang", weight=5),
                Stations(id="6", city="Jakarta", name="Stasiun Sudirman", weight=6),
                Stations(id="7", city="Jakarta", name="Stasiun Palmerah", weight=7),
                Stations(id="8", city="Jakarta", name="Stasiun Kebayoran", weight=8),
                Stations(id="9", city="Jakarta", name="Stasiun Pondok Ranji", weight=9),
                Stations(id="10", city="Jakarta", name="Stasiun Jurangmangu", weight=10),


                // Bekasi
                Stations(id="11", city="Bekas", name="Stasiun Cibatu", weight=25),
                Stations(id="12", city="Bekas", name="Stasiun Cikarang", weight=26),
                Stations(id="13", city="Bekas", name="Stasiun Tambun", weight=27),

                // Bandung
                Stations(id="14", city="Bandung", name="Stasiun Bandung", weight=50),
                Stations(id="15", city="Bandung", name="Stasiun Kiaracondong", weight=51),
                Stations(id="16", city="Bandung", name="Stasiun Cimahi", weight=52),
                Stations(id="17", city="Bandung", name="Stasiun Padalarang", weight=53),
                Stations(id="18", city="Bandung", name="Stasiun Cimindi", weight=54),
                Stations(id="19", city="Bandung", name="Stasiun Cikadongdong", weight=55),
                Stations(id="20", city="Bandung", name="Stasiun Cimolok", weight=56),
                Stations(id="21", city="Bandung", name="Stasiun Cilame", weight=57),

                // Cirebon
                Stations(id="22", city="Cirebon", name="Stasiun Cirebon", weight=60),
                Stations(id="23", city="Cirebon", name="Stasiun Cirebon Prujakan", weight=61),
                Stations(id="24", city="Cirebon", name="Stasiun Cirebon Kejaksan", weight=62),
                Stations(id="25", city="Cirebon", name="Stasiun Cirebon Tegalega", weight=63),

                // Tasikmalaya
                Stations(id="26", city="Tasikmalaya", name="Stasiun Tasikmalaya", weight=70),

                // Purwakarta
                Stations(id="27", city="Purwakarta", name="Stasiun Purwakarta", weight=80),
                Stations(id="28", city="Purwakarta", name="Stasiun Sadang", weight=81),

                // Semarang
                Stations(id="29", city="Semarang", name="Stasiun Semarang Poncol", weight=120),
                Stations(id="30", city="Semarang", name="Stasiun Semarang Tawang", weight=121),
                Stations(id="31", city="Semarang", name="Stasiun Semarang Pasar Tawang", weight=122),

                // Solo
                Stations(id="32", city="Solo", name="Stasiun Solo Balapan", weight=130),
                Stations(id="33", city="Solo", name="Stasiun Solo Jebres", weight=131),
                Stations(id="34", city="Solo", name="Stasiun Solo Purwosari", weight=132),

                // Kutoarjo
                Stations(id="35", city="Kutoarjo", name="Stasiun Kutoarjo", weight=134),
                Stations(id="36", city="Kutoarjo", name="Stasiun Kebumen", weight=135),

                // Purwokerto
                Stations(id="37", city="Purwokerto", name="Stasiun Purwokerto", weight=136),
                Stations(id="38", city="Purwokerto", name="Stasiun Kroya", weight=137),
                Stations(id="39", city="Purwokerto", name="Stasiun Kebasen", weight=138),
                Stations(id="40", city="Purwokerto", name="Stasiun Gombong", weight=139),


                // Yogyakarta
                Stations(id="45", city="Yogyakarta", name="Stasiun Yogyakarta", weight=140),
                Stations(id="46", city="Yogyakarta", name="Stasiun Lempuyangan", weight=141),
                Stations(id="47", city="Yogyakarta", name="Stasiun Wates", weight=142),
                Stations(id="48", city="Yogyakarta", name="Stasiun Maguwo", weight=143),
                Stations(id="49", city="Yogyakarta", name="Stasiun Klaten", weight=144),

                // Kediri
                Stations(id="50", city="Kediri", name="Stasiun Kediri", weight=150),
                Stations(id="51", city="Kediri", name="Stasiun Tulungagung", weight=151),

                // Malang
                Stations(id="52", city="Malang", name="Stasiun Malang", weight=160),
                Stations(id="53", city="Malang", name="Stasiun Malang Kota Lama", weight=161),
                Stations(id="54", city="Malang", name="Stasiun Malang Kotalama", weight=162),

                // Surabaya
                Stations(id="55", city="Surabaya", name="Stasiun Surabaya Pasarturi", weight=170),
                Stations(id="56", city="Surabaya", name="Stasiun Surabaya Gubeng", weight=171),
                Stations(id="57", city="Surabaya", name="Stasiun Surabaya Kota", weight=172),

                // Banyuwangi
                Stations(id="58", city="Banyuwangi", name="Stasiun Banyuwangi Baru", weight=180),
                Stations(id="59", city="Banyuwangi", name="Stasiun Karangasem", weight=181),
                Stations(id="60", city="Banyuwangi", name="Stasiun Rogojampi", weight=182),


            )

            listOfStation.forEach {
                fireConsole.stationsRef.add(it).addOnSuccessListener { documentReference ->
                    Log.i("FireSeeder", "Success adding station id : ${documentReference.id}")
                    it.id = documentReference.id
                    documentReference.set(it).addOnFailureListener {
                        println("Error adding station id : ${it.message}")
                    }
                }
            }
        }
    }
}