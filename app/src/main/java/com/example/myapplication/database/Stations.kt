package com.example.myapplication.database

import com.example.myapplication.model.Station

class Stations {
    val list = mutableListOf<Station>()

    init {
        list.add (
            Station(
                "a7925ec9-d198-48a1-97d7-972f50d6079e",
                "RBB Inforadio",
                "http://d141.rndfnk.com/ard/rbb/inforadio/live/mp3/48/stream.mp3?cid=01FC1WYDPN76K6HSCD8QCR9D3S&sid=2OaunhPrzFs3B9ZEKkGJWZcswLE&token=cyf7KyD9CCz0tTr0TkV1d3m-DvOGELfliMuW15xnZN8&tvf=auddWwESVxdkMTQxLnJuZGZuay5jb20",
                "https://www.inforadio.de/content/dam/rbb/rbb/logos/touch/inf-128.png",
                "52.49047383227829",
                "13.370361328125002",
            )
        )
        list.add (
            Station(
                "697f3001-9305-4fcb-b4ed-14cb8c817722",
                "X103.9",
                "https://18733.live.streamtheworld.com:443/KRXPFMAAC.aac?dist=onlineradiobox",
                "https://wpcdn.us-east-1.vip.tn-cloud.net/www.x1039radio.com/content/uploads/2021/05/b/v/cropped-tiny-logo-512x512-2021-180x180.png",
                "38.61180705362057",
                "-104.66760635375977",
            )
        )
        list.add (
            Station(
                "4a81f4a5-a207-4c5e-a19d-7bf8f3f51bf8",
                "br24 live (64 kbit/s)",
                "https://f131.rndfnk.com/ard/br/br24/live/mp3/128/stream.mp3?cid=01FBPVVJDPGF0BJ5TAJ8Z7JSX9&sid=2ObEfnnkwjZdMQ0IlMPIcityl7m&token=5F9qifR-t9LVi9IWh5x9IMoSIp1CwknPQDJhTrPBw1Y&tvf=clbpCuwaVxdmMTMxLnJuZGZuay5jb20",
                "https://www.br24.de/nachrichten/icons/apple-touch-icon.png?v=3",
                "48.13493370228957",
                "11.531524658203129",
            )
        )
    }
}