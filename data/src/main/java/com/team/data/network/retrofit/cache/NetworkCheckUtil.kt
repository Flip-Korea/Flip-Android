package com.team.data.network.retrofit.cache

/**
 * Network(Internet)에 연결된 상태 인지 확인한다.
 *
 * 해당 기능을 사용할 땐 ':app' 모듈에 있는 NetworkCheckImpl에서 DIP(의존성 역전 원칙)을 통해 주입된다.
 */
interface NetworkCheckUtil {
    fun hasNetwork(): Boolean
}