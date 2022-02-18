# Tubes Stima 1 by Overbrain
> _'Overdrive'_ car bot implementation written in Java. Based on the concept of Greedy algorithm.


## Table of Contents
* [Introduction](#introduction)
* [General Information](#general-information)
* [Technologies Used](#technologies-used)
* [Greedy Strategy](#greedy-strategy)
* [Screenshot](#screenshot)
* [Setup](#setup)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Library](#library)
* [Contact](#contact)


## Introduction
Hello, everyone! Welcome to our GitHub Repository!

This project was created by:
| No. | Name | Student ID |
| :---: | :---: | :---: |
| 1. | Eiffel Aqila Amarendra | 13520074 |
| 2. | Nelsen Putra | 13520130 |
| 3. | Willy Wilsen | 13520160 |


## General Information
_Overdrive_ is a game where 2 car bots compete in a racing event. Each player will have a car bot and each bot will compete with each other to reach the finish line and win the match. In order to win the match, each player must implement a certain strategy to be able to beat his opponent.

Our task here is to implement a car bot in the game _'Overdrive'_ with the use Greedy algorithm strategy to win the game. To implement With these bots, students are advised to use the available game engine and continue developing the program written in the starter-bots at in starter-pack.

This Overdrive car bot implementation program was developed in Java language. The game specification that was used to build this bot is based on the [game rules](https://github.com/EntelectChallenge/2020-Overdrive/blob/develop/game-engine/game-rules.md) that is provided on _'Overdrive'_ game engine.


## Technologies Used
The whole car bot implementation program was written in Java using IntelliJ IDEA.


## Greedy Strategy
Berdasarkan eksplorasi alternatif Algoritma Greedy di atas, kelompok kami memutuskan untuk menggabungkan strategi-strategi tersebut. Hal yang menjadi pertimbangan utama atas keputusan itu adalah fakta bahwa setiap strategi di atas hanya efektif dan dapat digunakan dalam beberapa kasus saja untuk setiap rondenya. Dengan demikian, penggabungan strategi-strategi tersebut dinilai lebih mampu secara efektif untuk menangani seluruh kasus atau skema yang ada selama permainan ketimbang memilih salah satu yang terbaik di antaranya.

Pada implementasi program, akhirnya kami menggabungkan dan memodifikasi kelima strategi tersebut. Untuk menangani kasus-kasus tertentu, kami berpikir bahwa diperlukan adanya pengaturan urutan prioritas strategi yang kemudian coba kami kombinasikan untuk menemukan mana kombinasi urutan yang paling optimal. Hasil yang kelompok dapatkan ialah kombinasi strategi dengan urutan sebagai berikut: strategi pemrioritasan perbaikan, strategi pemrioritasan penghindaran obstacle, strategi pemrioritasan pemaksimalan kecepatan, strategi pemrioritasan pencarian power-ups, dan yang terakhir strategi pemrioritasan pemanfaatan power-ups. Di samping itu, apabila berdasarkan command yang digunakan, urutan prioritasnya, yaitu perintah SAVETHECAR, AVOID, TURBO, MAXSPEED, GETPOWERUP, dan USEOIL/USEEMP/USETWEET.

Urutan prioritas yang kami tentukan terakhir kali berasal dari proses trial and error yang kami ujikan pada reference bot. Hasilnya, prioritas yang disusun demikian menghasilkan win rate tertinggi dibandingkan dengan variasi-variasi lainnya. Berdasarkan pengujian yang telah kami lakukan juga, terdapat kondisi-kondisi tertentu yang tidak dapat dihindari dalam mempengaruhi hasil akhir dari pertandingan mobil kami di permainan Overdrive ini, misalnya, bentuk atau kondisi maps yang dipakai pada pertandingan tersebut.


## Screenshot
{Insert screenshot here}


## Setup
### Installation
- Download and install [Java](https://www.oracle.com/java/technologies/downloads/#java8) (Min. Java 8) 
- Download and install [Java Development Kit](https://www.oracle.com/java/technologies/downloads/)
- Download and install [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Download and install [NodeJS](https://nodejs.org/en/download/)

### Requirement
- Install the whole modules and [libraries](#library) used in the source code
- Download the whole folders and files in this repository or do clone the repository
- Download Official Entelect Challenge 2020 Overdrive [Game Engine](https://github.com/EntelectChallenge/2020-Overdrive/releases/tag/2020.3.4)
- (Optional) Download Official Entelect Challenge 2020 Overdrive [Visualizer](https://github.com/Affuta/overdrive-round-runner)

### Setup
- Pastikan semua requirement di atas sudah terinstall pada perangkat keras yang akan digunakan.
- Perhatikan bahwa Game Engine yang diunduh dari link di atas merupakan starter pack yang digunakan oleh pemain untuk memulai membuat bot.
- Struktur folder starter pack tersebut dapat dilihat di https://github.com/EntelectChallenge/2020-Overdrive/releases/tag/2020.3.4.
- Lakukan pengimplementasian kode program menggunakan Intellij IDEA (dapat dilakukan dengan menjalankan file pom.xml).
- Setelah diimplementasikan, lakukan instalasi program dengan menggunakan Maven Toolbox pada bagian Lifecycle yang terletak di bagian kanan Intellij IDEA.
- Instalasi ini menghasilkan sebuah folder bernama target yang akan berisi sebuah file bernama java-sample-bot-jar-with-dependencies.jar.
- Pindahkan file ini ke dalam folder starter-pack. Jika sudah ada, file yang lama bisa digantikan dengan file yang baru ini.
- Pastikan konfigurasi program yang ada di game-runner-config.json sudah benar, meliputi direktori bot yang digunakan.
- Jika menggunakan file yang terdapat dalam repositori ini, maka yang perlu dilakukan adalah menggantikan file jar dengan file yang ada di folder bin.
- Selain itu, jangan lupa untuk tetap mengubah source code program dengan mengganti folder starter-bots dengan folder src yang ada di repositori ini.

### Execution
1. Clone this repository in your own local directory

    `git clone https://github.com/eiffelaqila/Tubes1_Overbrain.git`

2. Open the command line and change the directory to 'bin' folder

    `cd Tubes1_Overbrain/bin`
    
3. Run `java Main` on the command line


## Project Status
Project is: _complete_

All the specifications were implemented.


## Room for Improvement
- A faster or more efficient algorithm to make the program run quicker
- A superior strategy to increase the chances of winning the match


## Acknowledgements
- This project was based on [Spesifikasi Tugas Besar 1 Stima](https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2021-2022/Tugas-Besar-1-IF2211-Strategi-Algoritma-2022.pdf) and [Official Entelect Challenge 2020 Overdrive](https://github.com/EntelectChallenge/2020-Overdrive)
- Thanks to God
- Thanks to Mrs. Masayu Leylia Khodra, Mrs. Nur Ulfa Maulidevi, and Mr. Rinaldi as our lecturers
- Thanks to academic assistants
- This project was created to fulfill our Big Project for IF2211 Algorithm Strategies


## Library
- [time](https://www.tutorialspoint.com/c_standard_library/time_h.htm) (contoh doang, kalau gak ada atau gak mau tulis boleh apus)


## Contact
Created by Overbrain. 2022 All Rights Reserved.
