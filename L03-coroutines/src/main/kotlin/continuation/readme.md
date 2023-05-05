Continuation отвечает за сохранение состояния корутины и возможность дальнейшего продолжения работы из этого состояния.

- [simple suspend fun](#simple-suspend-fun)
- [suspend fun with delay](#suspend-fun-with-delay)

### simple suspend fun
kotlin kode
```kotlin
suspend fun myFunction() {
    println("Before")

    println("After")
}
```
wil be decompiled to java code:
```java
@Metadata(
   mv = {1, 8, 0},
   k = 2,
   xi = 48,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0011\u0010\u0000\u001a\u00020\u0001H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0002\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0003"},
   d2 = {"myFunction", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "m1l5-coroutines"}
)
public final class DecompileMePleaseKt {
   @Nullable
   public static final Object myFunction(@NotNull Continuation $completion) {
      System.out.println("Before");
      System.out.println("After");
      return Unit.INSTANCE;
   }
}
```

### suspend fun with delay
kotlin-code:
```kotlin
suspend fun myFunction() {
    println("Before")
    var counter = 0

    delay(1000) // suspending

    counter++
    println("After: $counter")
}
```

java-code:
```java
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlinx.coroutines.DelayKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(
   mv = {1, 8, 0},
   k = 2,
   xi = 48,
   d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u001a\u0011\u0010\u0000\u001a\u00020\u0001H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0002\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0003"},
   d2 = {"myFunction", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "m1l5-coroutines"}
)
public final class DecompileMePleaseKt {
   @Nullable
   public static final Object myFunction(@NotNull Continuation var0) {
      Object $continuation;
      label20: {
         if (var0 instanceof <undefinedtype>) {
            $continuation = (<undefinedtype>)var0;
            if ((((<undefinedtype>)$continuation).label & Integer.MIN_VALUE) != 0) {
               ((<undefinedtype>)$continuation).label -= Integer.MIN_VALUE;
               break label20;
            }
         }

         $continuation = new ContinuationImpl(var0) {
            int I$0;
            // $FF: synthetic field
            Object result;
            int label;

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = $result;
               this.label |= Integer.MIN_VALUE;
               return DecompileMePleaseKt.myFunction((Continuation)this);
            }
         };
      }

      Object $result = ((<undefinedtype>)$continuation).result;
      Object var4 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      int counter;
      switch(((<undefinedtype>)$continuation).label) {
      case 0:
         ResultKt.throwOnFailure($result);
         System.out.println("Before");
         counter = 0;
         ((<undefinedtype>)$continuation).I$0 = counter;
         ((<undefinedtype>)$continuation).label = 1;
         if (DelayKt.delay(1000L, (Continuation)$continuation) == var4) {
            return var4;
         }
         break;
      case 1:
         counter = ((<undefinedtype>)$continuation).I$0;
         ResultKt.throwOnFailure($result);
         break;
      default:
         throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      ++counter;
      System.out.println("After: " + counter);
      return Unit.INSTANCE;
   }
}

```