package com.example.weathercodechallenge.presentation

import androidx.datastore.preferences.core.stringPreferencesKey

const val PREFS_NAME = "main"
const val PREFS_KEY_CITY = "key_city"

val countrCodesMap: HashMap<String,String> = hashMapOf(
    "Albania" to "AL",
    "Algeria" to "DZ",
    "American Samoa" to "AS",
    "Andorra" to "AD",
    "Angola" to "AO",
    "Anguilla" to "AI",
    "Antarctica" to "AQ",
    "Antigua and Barbuda" to "AG",
    "Argentina" to "AR",
    "Armenia" to "AM",
    "Aruba" to "AW",
    "Australia" to "AU",
    "Austria" to "AT",
    "Azerbaijan" to "AZ",
    "Bahamas" to "BS",
    "Bahrai" to "BH",
    "Bangladesh" to "BD",
    "Barbados" to "BB",
    "Belarus" to "BY",
    "Belgium" to "BE",
    "Belize" to "BZ",
    "Benin" to "BJ",
    "Bermuda" to "BM",
    "Bhutan" to "BT",
    "Bolivia" to "BO",
    "Bonaire" to "BQ",
    "Bosnia and Herzegovina" to "BA",
    "Botswana" to "BW",
    "Bouvet Island" to "BV",
    "Brazil" to "BR",
    "British Indian Ocean Territory" to "IO",
    "British Virgin Islands" to "VG",
    "Brunei" to "BN",
    "Bulgaria" to "BG",
    "Burkina Faso" to "BF",
    "Burundi" to "BI",
    "Cambodia" to "KH",
    "Cameroon" to "CM",
    "Canada" to "CA",
    "Cape Verde	" to "CV",
    "Cayman Islands" to "KY",
    "Central African Republic" to "CF",
    "Chad" to "D",
    "Chile" to "CL",
    "China" to "CN",
    "Christmas Island" to "CX",
    "Cocos (Keeling) Islands" to "CC",
    "Colombia" to "CO",
    "Comoros" to "KM",
    "Cook Islands" to "CK",
    "Costa Rica" to "CR",
    "Cote D'Ivoire" to "CI",
    "Croatia" to "HR",
    "Cuba" to "CU",
    "Curacao" to "CW",
    "Cyprus" to "CY",
    "Czech Republic" to "CZ",
    "Democratic Republic of the Congo" to "CD",
    "Denmark" to "DK",
    "Djibouti" to "DJ",
    "Dominica" to "DM",
    "Dominican Republic" to "DO",
    "Ecuador" to "EC",
    "Egypt" to "EG",
    "El Salvador" to "SV",
    "Equatorial Guinea" to "GQ",
    "Eritrea" to "ER",
    "Estonia" to "EE",
    "Ethiopia" to "ET"
)