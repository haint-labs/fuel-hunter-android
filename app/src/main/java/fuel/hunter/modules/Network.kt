package fuel.hunter.modules

import fuel.hunter.FuelHunterServiceGrpcKt
import fuel.hunter.tools.di.Container
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import java.util.concurrent.Executor

fun Container.client(
    url: String = "162.243.16.251",
    port: Int = 50051,
    executor: Executor = Dispatchers.IO.asExecutor(),
    key: String = FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub::class.java.simpleName
) {
    val channel = ManagedChannelBuilder.forAddress(url, port)
        .usePlaintext()
        .executor(executor)
        .build()

    val client = FuelHunterServiceGrpcKt.FuelHunterServiceCoroutineStub(channel)

    single(key = key, value = client)
}