package com.example.tennis

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tennis.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_courts, R.id.navigation_map, R.id.navigation_starred, R.id.navigation_calendar
            )
        )
        
        val datalist = listOf("S231108093742281723", "S231108104359716676", "S231108110459780710", "S231108111805019183", "S200903171905941615", "S201030095955674906", "S201030100749834127", "S201030101119569374", "S201030101340919783", "S201030102944573871", "S201030103317125671", "S201030103931638215", "S201030105206531192", "S201030105601087749", "S201030145802586611", "S210302232236697326", "S210302232601011038", "S210302233348803598", "S210302233656019242", "S240402145153610347", "S240402150601237335", "S240430143839936049", "S240430145414714595", "S240430150047921130", "S240430150503521851", "S240430150822368081", "S240430151046982091", "S240430151417684178", "S240430151658501997", "S240430152144303358", "S240430152418826738", "S240430153704009963", "S240430155025638845", "S240430155449012609", "S240430155640134615", "S240430155834886998", "S240501114505940659", "S240501114936128268", "S240501115141520366", "S240501115337241532", "S240501115534137070", "S240502134147421761", "S231214145153394304", "S231214150453887673", "S231214152401675260", "S231215102655913101", "S240611150206484869", "S240611151952110533", "S240611152217386265", "S240611152504797745", "S240611152806595769", "S240611153254822946", "S240611153544907652", "S240611153802996339", "S240611154402975911", "S240611154647893152", "S240713143556491369", "S240713144057820321", "S240713144328428991", "S240713144619268074", "S240713145106621352", "S240713145410586735", "S240713145748548178", "S240713150011155082", "S240713150424044347", "S240713150626289865", "S231208113654873341", "S231208114038512386", "S190502151751379566", "S190502160848522070", "S220420104434156882", "S220420111459351786", "S210218174815551697", "S210219085555417413", "S210219091235657961", "S210219091554014528", "S210219091826906010", "S210219092004901944", "S210219092115226884", "S210219092313243896", "S210219092430823699", "S210219092625909779", "S210224094844227739", "S210224095950585838", "S210330144106815190", "S210330144415976466", "S240702121204490099", "S210330144745435127", "S210330150834331881", "S210330151503659748", "S210330153312040259", "S210330154443511753", "S210330154738706805", "S210330155032074618", "S210330161029473769", "S210330161319628636", "S240205084226331534", "S210205141719898815", "S210216130920543807", "S210216131232290211", "S240603085120538706", "S240603085859871694", "S240603090545616402", "S240603091017375874", "S240603095040766993", "S240603095357907518", "S240603095709563752", "S240603100010801243", "S240603111334845181", "S240603111628255642", "S240603111935482257", "S240603112240410985", "S240702101302503585", "S240702102347912628", "S240702102806010640", "S240702103308760066", "S240702121543862571", "S240702122036719070", "S240702122420995368", "S240702144057108512", "S240702144456524673", "S240702144828637795", "S240702145408140991", "S240605093759776679", "S240605094004498662", "S240702105644441549", "S240702105900849324", "S211228114752595461", "S211228114140285936", "S240318091140817195", "S240318091634106408", "S240318092032712339", "S240415111051795914", "S240415111739137324", "S240415112127461413", "S240514104140233985", "S240514104610200388", "S240514105028226815", "S240618092408183263", "S240618092813504619", "S240618093125493148", "S240717090402553386", "S240717090713934171", "S240717091046902660", "S231114091643721746", "S231124090620574246", "S231124143254807780", "S231124150238858287", "S231124150921076258", "S231127174523276322", "S231127174944025714", "S231127175843091098", "S231127180318293666", "S231127180612701976", "S240610103855844965", "S240610112027854676", "S240610132407749748", "S240610135649938319", "S240610141241570844", "S240713204938216820", "S240713205122179787", "S240713205255197993", "S240713205509230063", "S240713205708122423", "S240713210045353755", "S240713210641708695", "S240713210828133718", "S240713211201231715", "S240713211304624362", "S240713211420542340", "S240713211622773054", "S240713211912583127", "S240714212724335467", "S240714212844278295", "S240714212950916077", "S240714213344992087", "S240714213600989245", "S240714214018057031", "S240714214301044374", "S240714214434838063", "S240714214602436048", "S240714214846169225", "S240611163334280783", "S240611165126823850", "S240611170435000981", "S240611171252949285", "S240611172006544662", "S240611172859058362", "S240710151556624827", "S240710153731979250", "S240710154828885802", "S240710162900030383", "S240710164136090889", "S240710165312281747", "S240624010954519371", "S240624011720910973", "S240709161054332047", "S240709161334491024", "S240624012026925017", "S240624012957219343", "S240624013308528324", "S240624013538757717", "S240709162427491308", "S240709162751142497", "S240624013851946530", "S240624221302719992", "S240624221628001558", "S240627113408504088", "S240709163029454595", "S240709163509333827", "S240717235037514282", "S240718000356396218", "S240718001853917092", "S240718002451787670", "S240709164100888817", "S240709164301294781", "S240721214015268157", "S240721214926047494", "S240721215449104237", "S240721215726053356", "S240709164434954237", "S240709164602707480", "S240721215928268420", "S240721220308222948", "S240721220557824164", "S240709150128560072", "S240709155611684185", "S240709155926849794", "S240709160651552929", "S240709160904800497", "S240709164743623317", "S240709165045122610", "S240709165220501833", "S240724171255201905", "S240724175312146793", "S240724175624753646", "S240724180300711768", "S240724180516144096", "S240724180854847949", "S240724181958279599", "S240724182313716474", "S240724182629864571", "S240724182917345800", "S240724183151160038", "S240724183456988888", "S240724183703103165", "S240724183902868844", "S240724185044667168", "S240724185556398114", "S240724185729557645", "S240724185951364709", "S240625003057761417", "S240625004145644234", "S240625004933458409", "S240625005747338034", "S240725003101811846", "S240725005021729436", "S240725005924842592", "S240725010646207233", "S230428115408736358", "S231228105044310997", "S231228105825119348", "S231228110135781967", "S231228110236936539", "S231228110458451380", "S231228111323642118", "S231228111455048109", "S231230115642453701", "S231230120127104418", "S231230120835078727", "S240130210553822208", "S240603111455221550", "S240603111754257308", "S240603111929323569", "S240603112102564392", "S240603112237438958", "S240603112436337749", "S240603112616059251", "S240603112758364956", "S240603112939943947", "S240603113112528786", "S240603113426771423", "S240603113655600782", "S240603113823058430", "S240603113945703057", "S240603114112986344", "S240625112839876372", "S240625113450075832", "S240625113726444059", "S240625113941534789", "S240625114140604205", "S240625114322368812", "S240625114513386127", "S240625114708220837", "S240625114902430910", "S240613172429967748", "S240613173018606308", "S240613173356714002", "S240613173632806383", "S240613174159802991", "S240613174520845219", "S240613174922018447", "S240613175136034804", "S240613175329103459", "S240613175525235295", "S240613175730046827", "S240613175938441284", "S240613181150042673", "S240613181338692415", "S240613181521339959", "S231213121512817510", "S240208142205361141")

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}