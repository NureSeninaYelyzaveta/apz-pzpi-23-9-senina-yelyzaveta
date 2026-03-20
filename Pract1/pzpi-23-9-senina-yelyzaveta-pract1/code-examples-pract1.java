using System;
using System.Collections.Generic;

interface IObserver { void Update(string msg); }

class ConcreteObserver : IObserver
{
    string name;
    public ConcreteObserver(string n) { name = n; }
    public void Update(string msg) { Console.WriteLine(name + ": " + msg); }
}

class Subject
{
    List<IObserver> observers = new List<IObserver>();
    public void Add(IObserver o) { observers.Add(o); }
    public void Notify(string msg) { observers.ForEach(o => o.Update(msg)); }
}

class Program
{
    static void Main()
    {
        var s = new Subject();
        s.Add(new ConcreteObserver("A"));
        s.Add(new ConcreteObserver("B"));
        s.Notify("Стан змінено");
    }
}
