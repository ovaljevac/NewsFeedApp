package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("1", "BiH i EU potpisale sporazum o digitalnoj tranziciji", "Sporazum podrazumijeva usvajanje EU standarda u oblasti digitalne uprave, sigurnosti podataka i unapređenja infrastrukture, što će otvoriti vrata za dodatna sredstva i IT projekte.", null, "Politika", true, "Klix.ba", "14.04.2025."),
            NewsItem("2", "Zatvorena ulica kod Parlamenta zbog radova", "Privremena obustava saobraćaja planirana je do petka.", null, "Politika", false, "Avaz", "14.04.2025."),
            NewsItem("3", "Zmajevi srušili Francusku u spektaklu u Sarajevu", "Nogometna reprezentacija BiH slavila je 3:2 na prepunom Koševu u kvalifikacijama za Evropsko prvenstvo, što im značajno povećava šanse za plasman.", null, "Sport", true, "Sportsport.ba", "13.04.2025."),
            NewsItem("4", "Nova biciklistička staza u Tuzli", "Staza povezuje centar grada s univerzitetskim kampusom.", null, "Politika", false, "Tuzlanski.ba", "13.04.2025."),
            NewsItem("5", "AI sistem domaćeg startupa koristi se u evropskim bolnicama", "Bosanskohercegovačka platforma za detekciju rizika kod pacijenata uvedena je u više klinika u Austriji i Sloveniji, a najavljena je i saradnja sa WHO.", null, "Nauka/tehnologija", true, "ETF Tech", "13.04.2025."),
            NewsItem("6", "Isključenja vode u dijelu Ilidže", "Radovi planirani u ulici Hrasnička cesta.", null, "Politika", false, "Sarajevo.ba", "12.04.2025."),
            NewsItem("7", "Košarkaši BiH pobijedili Srbiju u prijateljskom meču", "Rezultat 89:83 ohrabruje uoči narednih kvalifikacija.", null, "Sport", false, "Sport Centar", "12.04.2025."),
            NewsItem("8", "Otvoren nacionalni centar za robotiku u Sarajevu", "Centar nudi edukaciju, istraživanja i pristup modernim industrijskim robotima, a otvoren je u saradnji sa više fakulteta i međunarodnih organizacija.", null, "Nauka/tehnologija", true, "Start.ba", "12.04.2025."),
            NewsItem("9", "U ponedjeljak izmjene na linijama GRAS-a", "Nove trase se odnose na naselja Dobrinja i Mojmilo.", null, "Politika", false, "Radio Sarajevo", "12.04.2025."),
            NewsItem("10", "Sarajevski tim razvio aplikaciju za simulaciju zemljotresa", "Nova aplikacija koristi 3D modeliranje kako bi simulirala uticaj zemljotresa na građevine i omogućila inženjerima bolju pripremu.", null, "Nauka/tehnologija", true, "Klix Tech", "11.04.2025."),
            NewsItem("11", "Promjene termina sportskih škola zbog Ramazana", "Treninzi pomjereni u jutarnjim i večernjim terminima.", null, "Sport", false, "Sport.ba", "11.04.2025."),
            NewsItem("12", "BiH uvela digitalne lične karte sa QR kodom", "Ministarstvo unutrašnjih poslova predstavilo je nove digitalne dokumente koje građani mogu koristiti za identifikaciju i pristup e-servisima.", null, "Politika", true, "N1", "11.04.2025."),
            NewsItem("13", "Održan maraton mladih inovatora u Mostaru", "Učestvovalo preko 60 srednjoškolaca iz cijele zemlje.", null, "Nauka/tehnologija", false, "Bljesak.info", "10.04.2025."),
            NewsItem("14", "Nogometaši Željezničara remizirali s Veležom", "Utakmica završena bez golova na stadionu Grbavica.", null, "Sport", false, "Sport Centar", "10.04.2025."),
            NewsItem("15", "Bh. naučnik nagrađen za otkriće u kvantnoj fizici", "Dr. Haris Kovačević dobio je međunarodnu nagradu za rad u oblasti kvantnih komunikacija i sigurnosti podataka.", null, "Nauka/tehnologija", true, "ETF News", "10.04.2025."),
            NewsItem("16", "Privremeno zatvorena ulica Ferhadija zbog manifestacije", "Zatvaranje na snazi u nedjelju od 10 do 16 sati.", null, "Politika", false, "Klix.ba", "10.04.2025."),
            NewsItem("17", "BiH i Crna Gora dogovorile zajednički projekt prekogranične saradnje", "Projekt uključuje izgradnju zajedničkog inovacionog centra za digitalne tehnologije u Pljevljima i Goraždu.", null, "Politika", true, "Al Jazeera Balkans", "09.04.2025."),
            NewsItem("18", "Reprezentacija BiH U-21 savladala Češku u Zenici", "Mladi Zmajevi pokazali solidnu igru i karakter.", null, "Sport", false, "N1 Sport", "09.04.2025."),
            NewsItem("19", "Prva laboratorija za bioinžinjering otvorena na ETF-u", "Laboratorija omogućava simulaciju rada ljudskih ćelija i testiranje nano-materijala za medicinsku primjenu.", null, "Nauka/tehnologija", true, "ETF Lab", "09.04.2025."),
            NewsItem("20", "Zatvoreni tuneli na putu prema Jablanici zbog odrona", "Saobraćaj preusmjeren preko Konjica dok se ne ukloni materijal.", null, "Politika", false, "Avaz", "08.04.2025.")
        )
    }

}