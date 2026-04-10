package com.example.shoes_app

object ProductRepository {
    val products = listOf(
        Product(
            id = "1",
            name = "Nike Air Jordan 1",
            brand = "Nike",
            price = 12000.0,
            category = "Nike",
            description = "The iconic Air Jordan 1 high top sneaker. A classic for basketball and street style.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/u_126abdf4-374a-4ec0-86fa-46511c53ca36,c_scale,fl_relative,w_1.0,h_1.0,al_c/960af863-4963-4696-8097-960600000000/air-jordan-1-mid-shoes-SQ97Z7.png",
            rating = 4.8,
            stockQuantity = 15
        ),
        Product(
            id = "2",
            name = "Adidas Ultraboost",
            brand = "Adidas",
            price = 15000.0,
            category = "Adidas",
            description = "Experience the ultimate comfort and energy return with Adidas Ultraboost running shoes.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/099de19774da49978971ad6d00bca876_9366/Ultraboost_1.0_Shoes_Black_HQ4199_01_standard.jpg",
            rating = 4.7,
            stockQuantity = 20
        ),
        Product(
            id = "3",
            name = "Puma RS-X",
            brand = "Puma",
            price = 8000.0,
            category = "Puma",
            description = "Bold, bulky and colorful. The Puma RS-X reimagines street style with its retro-futuristic design.",
            imageUrl = "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/371570/01/sv01/fnd/PNA/fmt/png/RS-X-Toys-Sneakers",
            rating = 4.5,
            stockQuantity = 10
        ),
        Product(
            id = "4",
            name = "Under Armour Hovr",
            brand = "Under Armour",
            price = 11000.0,
            category = "Under Armour",
            description = "UA HOVR technology provides a 'zero gravity feel' to maintain energy return that helps eliminate impact.",
            imageUrl = "https://underarmour.scene7.com/is/image/UnderArmour/3024151-001_DEFAULT?rp=standard&rs=ultra&qlt=75&fmt=jpg&wid=500&hei=500&size=500,500&bgc=f0f0f0&rect=0,0,500,500",
            rating = 4.6,
            stockQuantity = 12
        ),
        Product(
            id = "5",
            name = "Nike Air Max 270",
            brand = "Nike",
            price = 13500.0,
            category = "Nike",
            description = "The first lifestyle Air Max from Nike brings you style, comfort and a big attitude.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/awcy7bhmo4o2sl8ltzrr/air-max-270-shoes-VpYxwG.png",
            rating = 4.9,
            stockQuantity = 8
        ),
        Product(
            id = "6",
            name = "Adidas NMD_R1",
            brand = "Adidas",
            price = 14000.0,
            category = "Adidas",
            description = "A fusion of street style and performance technology. The Adidas NMD_R1 is made for the urban nomad.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/900ef69cd091406aa979af5e01003f6d_9366/NMD_R1_Shoes_Black_HQ4452_01_standard.jpg",
            rating = 4.4,
            stockQuantity = 25
        ),
        Product(
            id = "7",
            name = "Jordan Retro 4",
            brand = "Jordan",
            price = 18000.0,
            category = "Jordan",
            description = "One of the most popular Jordan models of all time, the Retro 4 features side ankle supports and innovative mesh.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/6090660e-ed31-4091-9003-0ec6009edcc4/air-jordan-4-retro-shoes-9v9v9v.png",
            rating = 4.9,
            stockQuantity = 5
        ),
        Product(
            id = "8",
            name = "Reebok Classic Leather",
            brand = "Reebok",
            price = 6500.0,
            category = "Reebok",
            description = "Timeless style. The Reebok Classic Leather stays true to its heritage with a clean, iconic design.",
            imageUrl = "https://images.reebok.com/image/upload/f_auto,q_auto,fl_lossy,c_fill,g_auto/900ef69cd091406aa979af5e01003f6d_9366/Classic_Leather_Shoes_White_GY0952_01_standard.jpg",
            rating = 4.3,
            stockQuantity = 30
        ),
        Product(
            id = "9",
            name = "Nike Pegasus 40",
            brand = "Nike",
            price = 10500.0,
            category = "Nike",
            description = "A springy ride for every run. The Pegasus’ familiar, just-for-you feel returns to help you accomplish your goals.",
            imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,q_auto:eco/df90660e-ed31-4091-9003-0ec6009edcc4/air-zoom-pegasus-40-road-running-shoes-9v9v9v.png",
            rating = 4.7,
            stockQuantity = 18
        ),
        Product(
            id = "10",
            name = "Adidas Forum Low",
            brand = "Adidas",
            price = 9000.0,
            category = "Adidas",
            description = "More than just a shoe, it's a statement. The Adidas Forum hit the scene in '84 and gained major love on both the hardwood and the music business.",
            imageUrl = "https://assets.adidas.com/images/h_840,f_auto,q_auto,fl_lossy,c_fill,g_auto/900ef69cd091406aa979af5e01003f6d_9366/Forum_Low_Shoes_White_FY7756_01_standard.jpg",
            rating = 4.5,
            stockQuantity = 22
        ),
        Product(
            id = "11",
            name = "Puma Suede Classic",
            brand = "Puma",
            price = 7000.0,
            category = "Puma",
            description = "The Suede hit the scene in 1968 and has been changing the game ever since.",
            imageUrl = "https://images.puma.com/image/upload/f_auto,q_auto,b_rgb:fafafa,w_2000,h_2000/global/374915/01/sv01/fnd/PNA/fmt/png/Suede-Classic-XXI-Sneakers",
            rating = 4.6,
            stockQuantity = 14
        ),
        Product(
            id = "12",
            name = "Under Armour Curry 10",
            brand = "Under Armour",
            price = 14500.0,
            category = "Under Armour",
            description = "The Curry 10 is light, grippy, and stable, helping you change direction and stop on a dime.",
            imageUrl = "https://underarmour.scene7.com/is/image/UnderArmour/3025620-001_DEFAULT?rp=standard&rs=ultra&qlt=75&fmt=jpg&wid=500&hei=500&size=500,500&bgc=f0f0f0&rect=0,0,500,500",
            rating = 4.8,
            stockQuantity = 7
        )
    )

    fun getProductById(id: String): Product? {
        return products.find { it.id == id }
    }
}
