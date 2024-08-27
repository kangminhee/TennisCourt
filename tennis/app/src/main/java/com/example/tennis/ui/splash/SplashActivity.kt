package com.example.tennis.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tennis.MainActivity
import com.example.tennis.R
import com.example.tennis.data.PlaceholderContent

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 데이터를 불러오기 시작
        PlaceholderContent.importData(
            onDataLoaded = {
                // 데이터가 로드 완료되면 MainActivity로 전환
                navigateToMainActivity()
            },
            onLoadingStatusChanged = { isLoading ->
                // 로딩 상태에 따라 UI를 조작할 필요가 있으면 여기에 처리
            }
        )
    }

    private fun navigateToMainActivity() {
        // MainActivity로 전환
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // SplashActivity를 종료하여 백스택에서 제거
    }
}
