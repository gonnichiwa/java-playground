import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DelegatingTest {

    @Test
    public void catdogsound(){
        AnimalDelegator ad = new AnimalDelegator();
        assertThat(ad.makeSound(AnimalKind.CAT), is("cat sound"));
        assertThat(ad.makeSound(AnimalKind.DOG), is("dog sound"));
    }
}

class AnimalDelegator {
    List<Animal> animals;

    public AnimalDelegator(){
        animals = new ArrayList<>();
        animals.add(new Cat());
        animals.add(new Dog());
    }

    public String makeSound(AnimalKind kind){
        return animals.stream()
                .filter(a -> a.isSameKind(kind))
                .findFirst().get().makeSound();
    }
}
class Cat implements Animal {

    @Override
    public String makeSound() {
        return "cat sound";
    }

    @Override
    public AnimalKind getKind() {
        return AnimalKind.CAT;
    }

    @Override
    public boolean isSameKind(AnimalKind animalKind) {
        return AnimalKind.CAT.equals(animalKind);
    }
}
class Dog implements Animal {
    @Override
    public String makeSound() {
        return "dog sound";
    }

    @Override
    public AnimalKind getKind() {
        return AnimalKind.DOG;
    }

    @Override
    public boolean isSameKind(AnimalKind animalKind) {
        return AnimalKind.DOG.equals(animalKind);
    }
}
interface Animal {
    String makeSound();
    AnimalKind getKind();
    boolean isSameKind(AnimalKind animalKind);
}

enum AnimalKind {
    CAT, DOG
}