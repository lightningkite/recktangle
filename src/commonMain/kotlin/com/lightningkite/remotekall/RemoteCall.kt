//package com.lightningkite.remotekall
//
//import com.lightningkite.kommunicate.HttpBody
//import com.lightningkite.kommunicate.HttpClient
//import com.lightningkite.kommunicate.HttpMethod
//import com.lightningkite.kommunicate.callSerializer
//import kotlinx.serialization.KSerializer
//import kotlin.reflect.KClass
//import kotlinx.serialization.SerializationStrategy
//import kotlinx.serialization.context.get
//import kotlinx.serialization.internal.UnitSerializer
//import kotlinx.serialization.json.JSON
//
//annotation class Throws(val types: KClass<out Throwable>)
//
//interface RemoteCall<T>{
//    val result: T?
//}
//
//interface RemoteCallResolver {
//    suspend fun <T> resolve(serializer: KSerializer<T>, remoteCall: RemoteCall<T>): T
//
//    object Local: RemoteCallResolver {
//        val invocations = HashMap<KClass<out RemoteCall<*>>, suspend (RemoteCall<*>) -> Any?>()
//        @Suppress("UNCHECKED_CAST")
//        override suspend fun <T> resolve(remoteCall: RemoteCall<T>): T {
//            try {
//                return remoteCall::class.invocation(remoteCall) as T
//            } catch(e:Throwable){
//                throw RemoteException(e::class, e.message, null)
//            }
//        }
//    }
//
//    class JsonEndpoint(val endpoint: String, val headers:Map<String, List<String>> = mapOf()): RemoteCallResolver {
//        override suspend fun <T> resolve(remoteCall: RemoteCall<T>): T {
//            HttpClient.callSerializer(
//                url = endpoint,
//                method = HttpMethod.POST,
//                body = HttpBody.serialize(
//                    strategy = JSON.context[remoteCall::class]!! as SerializationStrategy<RemoteCall<T>>,
//                    value = remoteCall
//                ),
//                headers = headers,
//                serializer = JSON,
//                strategy = JSON.context[remoteCall::class]!! as SerializationStrategy<RemoteCall<T>>
//            )
//        }
//    }
//
//    companion object {
//        var default: RemoteCallResolver = Local
////        val serializers = HashMap<KClass<out RemoteCall<*>>, SerializationStrategy<*>>()
//    }
//}
//
////Type is passed through a combination of [KClass.simpleName] and the @Throws annotation
//class RemoteException(
//    val type: KClass<out Throwable>?,
//    message: String? = null,
//    cause: Throwable? = null
//): Exception(message, cause)
//
//var <T> KClass<out RemoteCall<T>>.invocation:(suspend (RemoteCall<*>) -> Any?)
//    get() = RemoteCallResolver.Local.invocations[this]!!
//    set(value){
//        RemoteCallResolver.Local.invocations[this] = value
//    }
//
////@Suppress("UNCHECKED_CAST")
////var <T> KClass<out RemoteCall<T>>.returnSerializer:SerializationStrategy<T>
////    get() = RemoteCallResolver.serializers[this]!! as SerializationStrategy<T>
////    set(value){
////        RemoteCallResolver.serializers[this] = value
////    }
//
//suspend fun <T> RemoteCall<T>.invoke(resolver: RemoteCallResolver = RemoteCallResolver.default):T = resolver.resolve(this)