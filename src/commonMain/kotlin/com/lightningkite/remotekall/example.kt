//package com.lightningkite.remotekall
//
//import kotlinx.serialization.internal.UnitSerializer
//
//class Example(): RemoteCall<Unit>{
//    init{
//        this::class.returnSerializer = UnitSerializer
//    }
//}
//
//fun test(){
//    Example::class.returnSerializer.descriptor.name
//    Example::class.simpleName
//}