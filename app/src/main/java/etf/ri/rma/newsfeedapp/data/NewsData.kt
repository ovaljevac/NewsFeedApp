package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.model.NewsItem

object NewsData {
    fun getAllNews(): List<NewsItem> {
        return listOf(
            NewsItem("1", "Vlada FBiH usvojila zakon o digitalnoj transformaciji javnog sektora", "Novi zakon predviđa digitalizaciju usluga i bolju dostupnost informacija građanima kroz e-platforme u naredne tri godine.", null, "Politika", true, "Klix.ba", "14.04.2025."),
            NewsItem("2", "BiH remizirala protiv Finske u borbenoj utakmici pred punom Grbavicom", "Reprezentacija Bosne i Hercegovine pokazala borbenost, ali nije uspjela da savlada čvrstu ekipu Finske koja se odlično branila svih 90 minuta.", null, "Sport", false, "Sport.ba", "13.04.2025."),
            NewsItem("3", "Naučnici sa UNSA razvili novu metodu brzog detektovanja mikroorganizama", "Tim istraživača s Prirodno-matematičkog fakulteta razvio je inovativni način otkrivanja bakterija pomoću biosenzora baziranog na nanotehnologiji.", null, "Nauka/tehnologija", true, "Nauka.ba", "12.04.2025."),
            NewsItem("4", "Skupština usvojila rebalans budžeta za 2025. s fokusom na zdravstvo", "Rebalansom su povećana sredstva za javne bolnice i uvođenje elektronskog kartona za sve građane u narednih 6 mjeseci.", null, "Politika", false, "Avaz", "11.04.2025."),
            NewsItem("5", "Košarkaška reprezentacija BiH u sjajnoj formi pred kvalifikacije", "Tim predvođen selektorom Ivanom Petrovim ostvario je petu pobjedu zaredom na pripremnim utakmicama u Sarajevu i Mostaru.", null, "Sport", true, "Klix.ba", "10.04.2025."),
            NewsItem("6", "NASA najavila saradnju s bh. startupima na razvoju svemirskih senzora", "Predstavnici NASA-e potpisali su memorandum o saradnji s inovatorima iz BiH na razvoju novih detektora za misije na Mars.", null, "Nauka/tehnologija", false, "NASA.ba", "09.04.2025."),
            NewsItem("7", "Predloženi zakon o minimalnoj plati izazvao burnu raspravu u parlamentu", "Sindikati i poslodavci iznijeli su oprečna mišljenja o prijedlogu da minimalna plata bude 1200 KM već od jula ove godine.", null, "Politika", false, "Klix.ba", "08.04.2025."),
            NewsItem("8", "Nogometaši Sarajeva slavili protiv Borca u derbiju kola", "Utakmica je završena rezultatom 2:1, a ključni gol postigao je Amer Dedić u 87. minuti nakon sjajne asistencije Selimovića.", null, "Sport", false, "Sport.ba", "07.04.2025."),
            NewsItem("9", "Univerzitet u Tuzli dobio prvu laboratoriju za kvantnu komunikaciju", "Radi se o pionirskom projektu koji će omogućiti testiranje kvantnih mreža i sigurnog prenosa informacija između računara.", null, "Nauka/tehnologija", true, "Nauka.ba", "06.04.2025."),
            NewsItem("10", "Premijer najavio nove infrastrukturne projekte u ruralnim područjima", "Planira se asfaltiranje 120 kilometara puteva, kao i rekonstrukcija škola i domova zdravlja u 15 općina širom Federacije.", null, "Politika", false, "Avaz", "05.04.2025."),
            NewsItem("11", "BiH tenis: Džumhur i Bašić u četvrtfinalu ATP turnira u Istanbulu", "Obojica su slavila u osmini finala i tako izborila priliku za historijski plasman u polufinale turnira serije 250.", null, "Sport", false, "Sport.ba", "04.04.2025."),
            NewsItem("12", "Studenti ETF-a razvili mobilnu aplikaciju za rano upozoravanje na zemljotrese", "Aplikacija koristi akcelerometre mobilnih uređaja i algoritme mašinskog učenja za predikciju seizmičkih aktivnosti.", null, "Nauka/tehnologija", false, "Klix.ba", "03.04.2025."),
            NewsItem("13", "Zakon o javnim nabavkama usvojen sa 55 glasova ZA", "Zakon uvodi centralizovani sistem praćenja tendera i povećava transparentnost trošenja javnih sredstava.", null, "Politika", true, "Klix.ba", "02.04.2025."),
            NewsItem("14", "Ženska reprezentacija BiH u rukometu osigurala baraž za Evropsko prvenstvo", "Sjajnim nastupom protiv Češke izborile su šansu za historijski plasman na veliko takmičenje.", null, "Sport", false, "Sport.ba", "01.04.2025."),
            NewsItem("15", "BH TechLab predstavio novi AI sistem za automatsku procjenu štete na vozilima", "Sistem koristi analizu slike i bazu podataka za trenutnu procjenu troškova popravke nakon saobraćajnih nezgoda.", null, "Nauka/tehnologija", false, "Nauka.ba", "31.03.2025."),
            NewsItem("16", "Rasprava o ustavnim promjenama: Fokus na ravnopravnosti građana i entiteta", "Održana tematska sjednica na kojoj su iznesene inicijative za izmjene preambule i izbora članova Predsjedništva.", null, "Politika", false, "Avaz", "30.03.2025."),
            NewsItem("17", "Nogometaši Veleža srušili Zrinjski u derbiju Mostara", "Golovima Haskića i Radovića, Velež je slavio rezultatom 3:2 u jednoj od najuzbudljivijih utakmica ove sezone.", null, "Sport", false, "Klix.ba", "29.03.2025."),
            NewsItem("18", "Sarajevski studenti predstavili pametni kontejner koji prepoznaje otpad", "Uređaj koristi senzore i AI kako bi prepoznao vrstu otpada i optimizovao reciklažni proces u urbanim sredinama.", null, "Nauka/tehnologija", true, "ETF.ba", "28.03.2025."),
            NewsItem("19", "Federalna vlada donijela odluku o subvencijama za mlade bračne parove", "Mjera uključuje subvencije za stanove do 100.000 KM i olakšice pri rješavanju stambenih kredita za mlade porodice.", null, "Politika", false, "Klix.ba", "27.03.2025."),
            NewsItem("20", "Bosna i Hercegovina dobila organizaciju Balkanskog prvenstva u atletici", "Takmičenje će se održati na stadionu Kamberovića polje u Zenici, uz učešće 12 zemalja regiona.", null, "Sport", true, "Sport.ba", "26.03.2025.")
        )
    }
}