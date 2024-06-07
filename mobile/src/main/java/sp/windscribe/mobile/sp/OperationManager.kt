//package sp.windscribe.mobile.sp
//
//import kotlinx.coroutines.*
//
//class OperationManager {
//    companion object : CoroutineScope {
//        private val job = SupervisorJob()
//        override val coroutineContext = Dispatchers.IO + job
//
//        // Static function to start a long-running operation
//        fun startOperation(
//            operation: () -> Unit,
//        ) {
//            launch {
//                operation()
//            }
//        }
//
//        // Static function to cancel all coroutines
//        fun stopOperation() {
//            job.cancel()
//            println("Operation stopped")
//        }
//    }
//}