package com.example.shoes_app

object ProductRepository {

    val products: List<Product> = listOf(

        // ── NIKE ──────────────────────────────────────────────────────────────
        Product(
            id = "nike_001",
            name = "Nike Air Max 270",
            brand = "Nike",
            price = 12999.0,
            discount = 10,
            category = "Nike",
            description = "The Nike Air Max 270 delivers all-day comfort with its large Air unit in the heel. Lightweight mesh upper keeps your feet cool while the foam midsole cushions every step.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/skwgyqrbfzhu6oimce7t/air-max-270-shoes-2V5C4p.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/skwgyqrbfzhu6oimce7t/air-max-270-shoes-2V5C4p.png"
            ),
            rating = 4.5,
            sizes = listOf(6, 7, 8, 9, 10, 11),
            stockQuantity = 50
        ),

        Product(
            id = "nike_002",
            name = "Nike Air Force 1",
            brand = "Nike",
            price = 8999.0,
            discount = 0,
            category = "Nike",
            description = "The radiance lives on in the Nike Air Force 1, the b-ball OG that puts a fresh spin on what you know best: durably stitched overlays, clean finishes and the perfect amount of flash.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/b7d9211d-1785-49b3-b55e-d813bb53a656/air-force-1-07-shoes-WzNNBZ.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/b7d9211d-1785-49b3-b55e-d813bb53a656/air-force-1-07-shoes-WzNNBZ.png"
            ),
            rating = 4.8,
            sizes = listOf(6, 7, 8, 9, 10, 11, 12),
            stockQuantity = 80
        ),

        Product(
            id = "nike_003",
            name = "Nike React Infinity Run",
            brand = "Nike",
            price = 14999.0,
            discount = 15,
            category = "Nike",
            description = "More foam. More cushion. More stability. The Nike React Infinity Run is designed to help reduce injury and keep you on the run. Paired with Nike React foam for a soft, responsive feel.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/i1-665455a5-45de-40fb-945f-c1852b82400d/react-infinity-run-flyknit-3-road-running-shoes-CNdgfS.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/i1-665455a5-45de-40fb-945f-c1852b82400d/react-infinity-run-flyknit-3-road-running-shoes-CNdgfS.png"
            ),
            rating = 4.6,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 35
        ),

        Product(
            id = "nike_004",
            name = "Nike ZoomX Vaporfly",
            brand = "Nike",
            price = 19999.0,
            discount = 5,
            category = "Nike",
            description = "Built for record-breaking speed, the Nike ZoomX Vaporfly NEXT% features our most responsive foam and a carbon fiber plate that propels you forward with every stride.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/1b1d19b5-fc00-41e6-a7e4-4d90c0e3e6ca/zoomx-vaporfly-next-2-road-racing-shoes-Nwz4c6.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/1b1d19b5-fc00-41e6-a7e4-4d90c0e3e6ca/zoomx-vaporfly-next-2-road-racing-shoes-Nwz4c6.png"
            ),
            rating = 4.9,
            sizes = listOf(7, 8, 9, 10),
            stockQuantity = 20
        ),

        // ── ADIDAS ────────────────────────────────────────────────────────────
        Product(
            id = "adidas_001",
            name = "Adidas Ultraboost 22",
            brand = "Adidas",
            price = 16999.0,
            discount = 12,
            category = "Adidas",
            description = "Responsive cushioning meets stylish design. The Ultraboost 22 features Primeknit+ upper that moves with your foot and Boost midsole that returns energy with every step.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/4b78bc47-7c71-4a47-a5f4-adf800f22a9c/ultraboost-22-shoes.jpg",
            imageUrls = listOf(
                "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/4b78bc47-7c71-4a47-a5f4-adf800f22a9c/ultraboost-22-shoes.jpg"
            ),
            rating = 4.7,
            sizes = listOf(6, 7, 8, 9, 10, 11),
            stockQuantity = 45
        ),

        Product(
            id = "adidas_002",
            name = "Adidas Stan Smith",
            brand = "Adidas",
            price = 7499.0,
            discount = 0,
            category = "Adidas",
            description = "A legend since 1971. The Stan Smith is a clean, classic tennis shoe with a perforated 3-Stripes logo and a durable rubber outsole. Timeless style that never goes out of fashion.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/68ae7ea7-a4d4-4a27-aad5-ae3600e23f8a/stan-smith-shoes.jpg",
            imageUrls = listOf(
                "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/68ae7ea7-a4d4-4a27-aad5-ae3600e23f8a/stan-smith-shoes.jpg"
            ),
            rating = 4.5,
            sizes = listOf(6, 7, 8, 9, 10, 11, 12),
            stockQuantity = 60
        ),

        Product(
            id = "adidas_003",
            name = "Adidas NMD R1",
            brand = "Adidas",
            price = 11499.0,
            discount = 8,
            category = "Adidas",
            description = "Street-ready comfort meets bold style. The NMD R1 draws inspiration from adidas archive running shoes and layers in modern Boost cushioning for all-day energy return.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/1b7e0228-ddc6-4c07-b7bc-af4b00f9fb12/nmd_r1-shoes.jpg",
            imageUrls = listOf(
                "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/1b7e0228-ddc6-4c07-b7bc-af4b00f9fb12/nmd_r1-shoes.jpg"
            ),
            rating = 4.4,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 30
        ),

        Product(
            id = "adidas_004",
            name = "Adidas Gazelle",
            brand = "Adidas",
            price = 8999.0,
            discount = 5,
            category = "Adidas",
            description = "A true icon. With its suede upper, T-toe overlay and contrast 3-Stripes, the Gazelle has been a favourite since 1968. Reissued with original details intact.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3e22b326-acf2-4ef8-a741-af72013d1f24/gazelle-shoes.jpg",
            imageUrls = listOf(
                "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/3e22b326-acf2-4ef8-a741-af72013d1f24/gazelle-shoes.jpg"
            ),
            rating = 4.6,
            sizes = listOf(6, 7, 8, 9, 10, 11),
            stockQuantity = 40
        ),

        // ── PUMA ──────────────────────────────────────────────────────────────
        Product(
            id = "puma_001",
            name = "Puma RS-X³ Puzzle",
            brand = "Puma",
            price = 9999.0,
            discount = 20,
            category = "Puma",
            description = "Inspired by the technical running shoes of the '80s, the RS-X³ Puzzle takes the daring design language of the original RS series to new heights with bold colorblocking.",
            imageUrl = "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/373308/01/sv01/fnd/IND/fmt/png/RS-X%C2%B3-Puzzle-Sneakers",
            imageUrls = listOf(
                "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/373308/01/sv01/fnd/IND/fmt/png/RS-X%C2%B3-Puzzle-Sneakers"
            ),
            rating = 4.3,
            sizes = listOf(6, 7, 8, 9, 10),
            stockQuantity = 25
        ),

        Product(
            id = "puma_002",
            name = "Puma Suede Classic",
            brand = "Puma",
            price = 6499.0,
            discount = 0,
            category = "Puma",
            description = "The Puma Suede has been changing the game since 1968. It's been worn by basketball legends and hip-hop icons. Now it's your turn to make it your own.",
            imageUrl = "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/352634/75/sv01/fnd/IND/fmt/png/Suede-Classic-XXI-Sneakers",
            imageUrls = listOf(
                "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/352634/75/sv01/fnd/IND/fmt/png/Suede-Classic-XXI-Sneakers"
            ),
            rating = 4.4,
            sizes = listOf(6, 7, 8, 9, 10, 11),
            stockQuantity = 55
        ),

        Product(
            id = "puma_003",
            name = "Puma Nitro Elite",
            brand = "Puma",
            price = 13499.0,
            discount = 10,
            category = "Puma",
            description = "Built for speed. The Puma Nitro Elite features a NITROFOAM™ midsole and carbon fibre plate for explosive energy return. Engineered for elite performance runners.",
            imageUrl = "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/376173/01/sv01/fnd/IND/fmt/png/Fast-R-NITRO%E2%84%A2-Elite-Running-Shoes",
            imageUrls = listOf(
                "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/376173/01/sv01/fnd/IND/fmt/png/Fast-R-NITRO%E2%84%A2-Elite-Running-Shoes"
            ),
            rating = 4.6,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 18
        ),

        // ── UNDER ARMOUR ──────────────────────────────────────────────────────
        Product(
            id = "ua_001",
            name = "UA HOVR Phantom 3",
            brand = "Under Armour",
            price = 14999.0,
            discount = 10,
            category = "Under Armour",
            description = "UA HOVR technology provides 'zero-gravity feel' to maintain energy return and help eliminate impact. Connected running sensor tracks your metrics automatically.",
            imageUrl = "https://underarmour.scene7.com/is/image/UnderArmour/3026577-100_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500",
            imageUrls = listOf(
                "https://underarmour.scene7.com/is/image/UnderArmour/3026577-100_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500"
            ),
            rating = 4.5,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 22
        ),

        Product(
            id = "ua_002",
            name = "UA Charged Assert 10",
            brand = "Under Armour",
            price = 5999.0,
            discount = 15,
            category = "Under Armour",
            description = "Lightweight mesh upper provides breathability. Charged Cushioning® midsole absorbs impact and converts it into a responsive push-off. Durable rubber outsole.",
            imageUrl = "https://underarmour.scene7.com/is/image/UnderArmour/3024590-001_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500",
            imageUrls = listOf(
                "https://underarmour.scene7.com/is/image/UnderArmour/3024590-001_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500"
            ),
            rating = 4.2,
            sizes = listOf(6, 7, 8, 9, 10, 11),
            stockQuantity = 70
        ),

        Product(
            id = "ua_003",
            name = "UA SlipSpeed Mega",
            brand = "Under Armour",
            price = 7999.0,
            discount = 0,
            category = "Under Armour",
            description = "Train hard, recover fast. The UA SlipSpeed Mega features a foldable heel collar that converts from a high-top to a slide for ultimate versatility.",
            imageUrl = "https://underarmour.scene7.com/is/image/UnderArmour/3027169-100_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500",
            imageUrls = listOf(
                "https://underarmour.scene7.com/is/image/UnderArmour/3027169-100_DEFAULT?rp=standard-0pad|pdpZoomDesktop&scl=0.72&fmt=jpg&qlt=85&resMode=sharp2&cache=on,on&bgc=f0f0f0&wid=1836&hei=1380&size=1500,1500"
            ),
            rating = 4.3,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 30
        ),

        // ── JORDAN ────────────────────────────────────────────────────────────
        Product(
            id = "jordan_001",
            name = "Air Jordan 1 Retro High",
            brand = "Jordan",
            price = 17999.0,
            discount = 0,
            category = "Jordan",
            description = "The Air Jordan 1 Retro High OG is the shoe that started it all. Banned from the NBA in 1985, it became an icon on AND off the court. Premium leather and Air cushioning.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/i1-e9d23b54-4a3a-4d26-be58-c3e8e3618f1c/air-jordan-1-retro-high-og-shoes-7WKFSk.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/i1-e9d23b54-4a3a-4d26-be58-c3e8e3618f1c/air-jordan-1-retro-high-og-shoes-7WKFSk.png"
            ),
            rating = 4.9,
            sizes = listOf(7, 8, 9, 10, 11, 12),
            stockQuantity = 15
        ),

        Product(
            id = "jordan_002",
            name = "Air Jordan 4 Retro",
            brand = "Jordan",
            price = 19999.0,
            discount = 0,
            category = "Jordan",
            description = "Inspired by MJ's explosive performance during the 1989 playoffs, the Jordan 4 Retro features visible Air cushioning, mesh detailing and the Flight logo on the tongue.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/14bb20b4-9a3a-4e11-8504-9a40090f13c8/air-jordan-4-retro-shoes-2fvJdz.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/14bb20b4-9a3a-4e11-8504-9a40090f13c8/air-jordan-4-retro-shoes-2fvJdz.png"
            ),
            rating = 4.8,
            sizes = listOf(7, 8, 9, 10, 11),
            stockQuantity = 10
        ),

        Product(
            id = "jordan_003",
            name = "Air Jordan 11 Concord",
            brand = "Jordan",
            price = 24999.0,
            discount = 0,
            category = "Jordan",
            description = "Michael Jordan wore the Jordan 11 to his first NBA comeback game. Patent leather mudguard and carbon-fiber midsole plate make this one of the most iconic sneakers ever made.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/e36c68f4-6f0a-47a3-9e63-6ba5ceaba1bb/air-jordan-11-retro-shoes-nqRXtK.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/e36c68f4-6f0a-47a3-9e63-6ba5ceaba1bb/air-jordan-11-retro-shoes-nqRXtK.png"
            ),
            rating = 5.0,
            sizes = listOf(8, 9, 10, 11),
            stockQuantity = 8
        ),

        Product(
            id = "jordan_004",
            name = "Air Jordan 3 Retro",
            brand = "Jordan",
            price = 18999.0,
            discount = 5,
            category = "Jordan",
            description = "The Air Jordan 3 was the first Jordan designed by Tinker Hatfield and the first to feature the Jumpman logo. Elephant print detailing and visible Air cushioning are the hallmarks.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/6e57a8be-d24a-4c5e-9b89-e8ae9b1c5042/air-jordan-3-retro-shoes-fJByNd.png",
            imageUrls = listOf(
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/6e57a8be-d24a-4c5e-9b89-e8ae9b1c5042/air-jordan-3-retro-shoes-fJByNd.png"
            ),
            rating = 4.9,
            sizes = listOf(7, 8, 9, 10, 11, 12),
            stockQuantity = 12
        )
    )

    fun getProductById(id: String): Product? = products.find { it.id == id }
}
