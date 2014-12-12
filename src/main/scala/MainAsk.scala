object AkkaPatternAsk {
	import akka.actor._

	object Messages {
		case class Square(n: Int)
		case class Resp(n: Int)
	}

	class ActorA extends Actor {
		import scala.concurrent._
		import scala.concurrent.duration._
		import scala.util.{Success, Failure}
		import scala.util.control.NonFatal
		import akka.util.Timeout
		import akka.pattern.ask
		// import akka.pattern.pipe // pipeTo
		import context._ // pula wątków (dispatcher)

		implicit val timeout = Timeout(5.seconds)

		import Messages._
		def receive = {
			case Square(n) =>
				val sndr = sender()
				val b = context.actorOf(Props[Squarer], "squarer")
				val respFuture = b.ask(Square(n)).mapTo[Resp]
				respFuture.onComplete {
					case Success(Resp(n)) =>
						println(s"Podwykonawca zwrócił $n")
						sndr ! Resp(n)
					case Failure(NonFatal(e)) =>
						println("Podwykonawca nie poradził sobie z zadaniem…")
				}
		}
	}

	class Squarer extends Actor {
		import Messages._
		def receive = {
			case Square(n) =>
				sender() ! Resp(n * n)
				self ! PoisonPill
		}
	}

	class MainActor extends Actor {
		import Messages._
		val worker = context.actorOf(Props[ActorA], "worker")
		worker ! Square(5)

		def receive = {
			case Resp(n) =>
				println(s"Wynik to $n")
				context.system.shutdown()
		}
	}

	def main(args: Array[String]): Unit = {
		import Messages._
		val sys = ActorSystem("system")
		val main = sys.actorOf(Props[MainActor], "main")
	}
}