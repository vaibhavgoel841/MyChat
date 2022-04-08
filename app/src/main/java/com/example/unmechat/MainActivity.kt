package com.example.unmechat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager.adapter = ScreenSlideAdapter(this)
        TabLayoutMediator(tablayout, viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, i: Int ->
when(i){
    0->tab.text="Chats"
    1->tab.text="People"
}
            }
        ).attach()

    }
}