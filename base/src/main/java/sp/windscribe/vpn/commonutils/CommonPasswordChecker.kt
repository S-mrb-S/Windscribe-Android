package sp.windscribe.vpn.commonutils

class CommonPasswordChecker {
    companion object {
        private val commonPasswords = listOf(
                "12345",
                "123456",
                "123456789",
                "test1",
                "password",
                "12345678",
                "zinch",
                "g_czechout",
                "asdf",
                "qwerty",
                "1234567890",
                "1234567",
                "Aa123456.",
                "iloveyou",
                "1234",
                "abc123",
                "111111",
                "123123",
                "dubsmash",
                "test",
                "princess",
                "qwertyuiop",
                "sunshine",
                "BvtTest123",
                "11111",
                "ashley",
                "00000",
                "000000",
                "password1",
                "monkey",
                "livetest",
                "55555",
                "soccer",
                "charlie",
                "asdfghjkl",
                "654321",
                "family",
                "michael",
                "123321",
                "football",
                "baseball",
                "q1w2e3r4t5y6",
                "nicole",
                "jessica",
                "purple",
                "shadow",
                "hannah",
                "chocolate",
                "michelle",
                "daniel",
                "maggie",
                "qwerty123",
                "hello",
                "112233",
                "jordan",
                "tigger",
                "666666",
                "987654321",
                "superman",
                "12345678910",
                "summer",
                "1q2w3e4r5t",
                "fitness",
                "bailey",
                "zxcvbnm",
                "fuckyou",
                "121212",
                "buster",
                "butterfly",
                "dragon",
                "jennifer",
                "amanda",
                "justin",
                "cookie",
                "basketball",
                "shopping",
                "pepper",
                "joshua",
                "hunter",
                "ginger",
                "matthew",
                "abcd1234",
                "taylor",
                "samantha",
                "whatever",
                "andrew",
                "1qaz2wsx3edc",
                "thomas",
                "jasmine",
                "animoto",
                "madison",
                "987654321",
                "54321",
                "flower",
                "Password",
                "maria",
                "babygirl",
                "lovely",
                "sophie",
                "Chegg123",
                "computer",
                "qwe123",
                "anthony",
                "1q2w3e4r",
                "peanut",
                "bubbles",
                "asdasd",
                "qwert",
                "1qaz2wsx",
                "pakistan",
                "123qwe",
                "liverpool",
                "elizabeth",
                "harley",
                "chelsea",
                "familia",
                "yellow",
                "william",
                "george",
                "7777777",
                "loveme",
                "123abc",
                "letmein",
                "oliver",
                "batman",
                "cheese",
                "banana",
                "testing",
                "secret",
                "angel",
                "friends",
                "jackson",
                "aaaaaa",
                "softball",
                "chicken",
                "lauren",
                "andrea",
                "welcome",
                "asdfgh",
                "robert",
                "orange",
                "Testing1",
                "pokemon",
                "555555",
                "melissa",
                "morgan",
                "123123123",
                "qazwsx",
                "diamond",
                "brandon",
                "jesus",
                "mickey",
                "olivia",
                "changeme",
                "danielle",
                "victoria",
                "gabriel",
                "123456a",
                "0.00000000",
                "loveyou",
                "hockey",
                "freedom",
                "azerty",
                "snoopy",
                "skinny",
                "myheritage",
                "qwerty1",
                "159753",
                "forever",
                "iloveu",
                "killer",
                "joseph",
                "master",
                "mustang",
                "hellokitty",
                "school",
                "Password1",
                "patrick",
                "blink182",
                "tinkerbell",
                "rainbow",
                "nathan",
                "cooper",
                "onedirection",
                "alexander",
                "jordan23",
                "lol123",
                "jasper",
                "junior",
                "q1w2e3r4",
                "222222",
                "11111111",
                "benjamin",
                "jonathan",
                "passw0rd",
                "123456789",
                "a123456",
                "samsung",
                "123",
                "love123",
                "123456",
                "123456789",
                "picture1",
                "password",
                "12345678",
                "111111",
                "123123",
                "1234567890",
                "senha",
                "1234567",
                "qwerty",
                "abc123",
                "Million2",
                "000000",
                "1234",
                "iloveyou",
                "aaron431",
                "password1",
                "qqww1122",
                "123",
                "omgpop",
                "123321",
                "654321",
                "qwertyuiop",
                "qwer123456",
                "123456a",
                "a123456",
                "666666",
                "asdfghjkl",
                "ashley",
                "987654321",
                "unknown",
                "zxcvbnm",
                "112233",
                "chatbooks",
                "20100728",
                "123123123",
                "princess",
                "jacket025",
                "evite",
                "123abc",
                "123qwe",
                "sunshine",
                "121212",
                "dragon",
                "1q2w3e4r",
                "5201314",
                "159753",
                "123456789",
                "pokemon",
                "qwerty123",
                "Bangbang123",
                "jobandtalent",
                "monkey",
                "1qaz2wsx",
                "abcd1234",
                "default",
                "aaaaaa",
                "soccer",
                "123654",
                "ohmnamah23",
                "12345678910",
                "zing",
                "shadow",
                "102030",
                "11111111",
                "asdfgh",
                "147258369",
                "qazwsx",
                "qwe123",
                "michael",
                "football",
                "baseball",
                "1q2w3e4r5t",
                "party",
                "daniel",
                "asdasd",
                "222222",
                "myspace1",
                "asd123",
                "555555",
                "a123456789",
                "888888",
                "7777777",
                "fuckyou",
                "1234qwer",
                "superman",
                "147258",
                "999999",
                "159357",
                "love123",
                "tigger",
                "purple",
                "samantha",
                "charlie",
                "babygirl",
                "88888888",
                "jordan23",
                "789456123",
                "jordan",
                "anhyeuem",
                "killer",
                "basketball",
                "michelle",
                "1q2w3e",
                "lol123",
                "qwerty1",
                "789456",
                "6655321",
                "nicole",
                "naruto",
                "master",
                "chocolate",
                "maggie",
                "computer",
                "hannah",
                "jessica",
                "123456789a",
                "password123",
                "hunter",
                "686584",
                "iloveyou1",
                "987654321",
                "justin",
                "cookie",
                "hello",
                "blink182",
                "andrew",
                "25251325",
                "love",
                "987654",
                "bailey",
                "princess1",
                "123456",
                "101010",
                "12341234",
                "a801016",
                "1111",
                "1111111",
                "anthony",
                "yugioh",
                "fuckyou1",
                "amanda",
                "asdf1234",
                "trustno1",
                "butterfly",
                "x4ivygA51F",
                "iloveu",
                "batman",
                "starwars",
                "summer",
                "michael1",
                "00000000",
                "lovely",
                "jakcgt333",
                "buster",
                "jennifer",
                "babygirl1",
                "family",
                "456789",
                "azerty",
                "andrea",
                "q1w2e3r4",
                "qwer1234",
                "hello123",
                "10203",
                "matthew",
                "pepper",
                "12345a",
                "letmein",
                "joshua",
                "131313",
                "123456b",
                "madison",
                "Sample123",
                "777777",
                "football1",
                "jesus1",
                "taylor",
                "b123456",
                "whatever",
                "welcome",
                "ginger",
                "flower",
                "333333",
                "1111111111",
                "robert",
                "samsung",
                "a12345",
                "loveme",
                "gabriel",
                "alexander",
                "cheese",
                "passw0rd",
                "142536",
                "peanut",
                "11223344",
                "thomas",
                "angel1",
                "123456789",
                "12345",
                "qwerty",
                "password",
                "12345678",
                "111111",
                "123123",
                "1234567890",
                "senha",
                "1234567",
                "qwerty",
                "abc123",
                "Million2",
                "000000",
                "1234",
                "iloveyou",
                "aaron431",
                "password1",
                "qqww1122",
                "123",
                "omgpop",
                "123321",
                "654321",
                "qwertyuiop",
                "qwer123456",
                "123456a",
                "a123456",
                "666666",
                "asdfghjkl",
                "ashley",
                "987654321",
                "unknown",
                "zxcvbnm",
                "112233",
                "chatbooks",
                "20100728",
                "123123123",
                "princess",
                "jacket025",
                "evite",
                "123abc",
                "123qwe",
                "sunshine",
                "121212",
                "dragon",
                "1q2w3e4r",
                "5201314",
                "159753",
                "123456789",
                "pokemon",
                "qwerty123",
                "Bangbang123",
                "jobandtalent",
                "monkey",
                "1qaz2wsx",
                "abcd1234",
                "default",
                "aaaaaa",
                "soccer",
                "123654",
                "ohmnamah23",
                "12345678910",
                "zing",
                "shadow",
                "102030",
                "11111111",
                "asdfgh",
                "147258369",
                "qazwsx",
                "qwe123",
                "michael",
                "football",
                "baseball",
                "1q2w3e4r5t",
                "party",
                "daniel",
                "asdasd",
                "222222",
                "myspace1",
                "asd123",
                "555555",
                "a123456789",
                "888888",
                "7777777",
                "fuckyou",
                "1234qwer",
                "superman",
                "147258",
                "999999",
                "159357",
                "love123",
                "tigger",
                "purple",
                "samantha",
                "charlie",
                "babygirl",
                "88888888",
                "jordan23",
                "789456123",
                "jordan",
                "anhyeuem",
                "killer",
                "basketball",
                "michelle",
                "1q2w3e",
                "lol123",
                "qwerty1",
                "789456",
                "6655321",
                "nicole",
                "naruto",
                "master",
                "chocolate",
                "maggie",
                "computer",
                "hannah",
                "jessica",
                "123456789a",
                "password123",
                "hunter",
                "686584",
                "iloveyou1",
                "987654321",
                "justin",
                "cookie",
                "hello",
                "blink182",
                "andrew",
                "25251325",
                "love",
                "987654",
                "bailey",
                "princess1",
                "123456",
                "101010",
                "12341234",
                "a801016",
                "1111",
                "1111111",
                "anthony",
                "yugioh",
                "fuckyou1",
                "amanda",
                "asdf1234",
                "trustno1",
                "butterfly",
                "x4ivygA51F",
                "iloveu",
                "batman",
                "starwars",
                "summer",
                "michael1",
                "00000000",
                "lovely",
                "jakcgt333",
                "buster",
                "jennifer",
                "babygirl1",
                "family",
                "456789",
                "azerty",
                "andrea",
                "q1w2e3r4",
                "qwer1234",
                "hello123",
                "10203",
                "matthew",
                "pepper",
                "12345a",
                "letmein",
                "joshua",
                "131313",
                "123456b",
                "madison",
                "Sample123",
                "777777",
                "football1",
                "jesus1",
                "taylor",
                "b123456",
                "whatever",
                "welcome",
                "ginger",
                "flower",
                "333333",
                "1111111111",
                "robert",
                "samsung",
                "a12345",
                "loveme",
                "gabriel",
                "alexander",
                "cheese",
                "passw0rd",
                "142536",
                "peanut",
                "11223344",
                "thomas",
                "angel1"
        )

        fun isAMatch(password: String): Boolean {
            return commonPasswords.contains(password.lowercase())
        }
    }
}
