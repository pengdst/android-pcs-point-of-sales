package io.github.pengdst.salescashier

import org.junit.Test

class StackUnitTest {

    @Test
    fun staticStack() /* using array */ {

    }

    @Test
    fun dynamicStack() /* or linked-list */ {
        val stack = LinkedListStack()

        stack.push(1)
        stack.push(2)
        stack.push(3)
        stack.push(4)

        print(stack.isEmpty)
    }

    @Test
    fun staticQueue() /* using array */ {

    }

    @Test
    fun dynamicQueue() /* or linked-list */ {
        val stack = LinkedListQueue()

        stack.enqueue(1)
        stack.enqueue(2)
        stack.enqueue(3)
        stack.enqueue(4)

        print(stack.isEmpty)
    }

    class StaticStack(size: Int) {

        private val arr = IntArray(size)
        private var top = -1
        private val capacity = size

        // Utility function to add an element `x` to the stack
        fun push(x: Int) {
            if (isFull) {
                println("Overflow\nProgram Terminated\n")
                print(-1)
                return
            }
            println("Inserting $x")
            arr[++top] = x
        }

        // Utility function to pop a top element from the stack
        fun pop(): Int {
            // check for stack underflow
            if (isEmpty) {
                println("Underflow\nProgram Terminated")
                print(-1)
                return -1
            }
            println("Removing " + peek())

            // decrease stack size by 1 and (optionally) return the popped element
            return arr[top--]
        }

        // Utility function to return the top element of the stack
        fun peek(): Int {
            if (!isEmpty) {
                return arr[top]
            } else {
                println(-1)
            }
            return -1
        }

        // Utility function to return the size of the stack
        fun size(): Int {
            return top + 1
        }// or return size() == 0;

        // Utility function to check if the stack is empty or not
        val isEmpty: Boolean
            get() = top == -1 // or return size() == 0;
            // or return size() == capacity;

        // Utility function to check if the stack is full or not
        val isFull: Boolean
            get() = top == capacity - 1 // or return size() == capacity;

    }

    class LinkedListStack {

        data class Node(
            var data: Int = 0,
            var next: Node? = null
        )

        private var top: Node? = null
        private var nodesCount = 0

        // Utility function to add an element `x` to the stack
        fun push(x: Int) // insert at the beginning
        {
            // allocate a new node in a heap
            val node = Node()

            // check if stack (heap) is full. Then inserting an element would
            // lead to stack overflow
            if (node == null) {
                println("Heap Overflow")
                return
            }
            println("Inserting $x")

            // set data in the allocated node
            node.data = x

            // set the .next pointer of the new node to point to the current
            // top node of the list
            node.next = top

            // update top pointer
            top = node

            // increase stack's size by 1
            nodesCount += 1
        }

        // Utility function to return the top element of the stack
        fun peek(): Int {
            // check for an empty stack
            if (isEmpty) {
                println("The stack is empty")
                println(-1)
            }
            return top?.data ?: -1
        }

        // Utility function to pop a top element from the stack
        fun pop(): Int // remove at the beginning
        {
            // check for stack underflow
            if (isEmpty) {
                println("Stack Underflow")
                println(-1)
            }

            // take note of the top node's data
            val top = peek()
            println("Removing $top")

            // decrease stack's size by 1
            nodesCount -= 1

            // update the top pointer to point to the next node
            this.top = this.top?.next
            return top
        }

        // Utility function to check if the stack is empty or not
        val isEmpty: Boolean
            get() = top == null

        fun search(element: Int): Int {
            var temp = top
            var i = 1
            while (temp?.next == null) {
                if (temp?.data == element) return i
                i++
                temp = temp?.next
            }
            return -1
        }

        // Utility function to return the size of the stack
        fun size(): Int {
            return nodesCount
        }
    }

    class StaticQueue(size: Int) {
        private val arr: IntArray = IntArray(size)

        private var front: Int
        private var rear: Int

        private val capacity: Int = size

        private var count: Int

        // Utility function to dequeue the front element
        fun dequeue(): Int {
            // check for queue underflow
            if (isEmpty) {
                println("Underflow\nProgram Terminated")
                println(-1)
            }
            val x = arr[front]
            println("Removing $x")
            front = (front + 1) % capacity
            count--
            return x
        }

        // Utility function to add an item to the queue
        fun enqueue(item: Int) {
            // check for queue overflow
            if (isFull) {
                println("Overflow\nProgram Terminated")
                println(-1)
                return
            }
            println("Inserting $item")
            rear = (rear + 1) % capacity
            arr[rear] = item
            count++
        }

        // Utility function to return the front element of the queue
        fun peek(): Int {
            if (isEmpty) {
                println("Underflow\nProgram Terminated")
                println(-1)
            }
            return arr[front]
        }

        // Utility function to return the size of the queue
        fun size(): Int {
            return count
        }

        // Utility function to check if the queue is empty or not
        val isEmpty: Boolean
            get() = size() == 0

        // Utility function to check if the queue is full or not
        val isFull: Boolean
            get() = size() == capacity

        // Constructor to initialize a queue
        init {
            front = 0
            rear = -1
            count = 0
        }
    }

    class LinkedListQueue {

        data class Node (  // integer data
            var data: Int,
            var next: Node? = null
        )

        private var rear: Node? = null
        private var front: Node? = null
        private var count = 0

        // Utility function to dequeue the front element
        fun dequeue(): Int // delete at the beginning
        {
            if (front == null) {
                println("\nQueue Underflow")
                println(-1)
            }
            val temp = front
            System.out.printf("Removing %d\n", temp?.data)

            // advance front to the next node
            front = front?.next

            // if the list becomes empty
            if (front == null) {
                rear = null
            }

            // decrease the node's count by 1
            count -= 1

            // return the removed item
            return temp?.data ?: -1
        }

        // Utility function to add an item to the queue
        fun enqueue(item: Int) // insertion at the end
        {
            // allocate a new node in a heap
            val node = Node(item)
            System.out.printf("Inserting %d\n", item)

            // special case: queue was empty
            if (front == null) {
                // initialize both front and rear
                front = node
                rear = node
            } else {
                // update rear
                rear?.next = node
                rear = node
            }

            // increase the node's count by 1
            count += 1
        }

        // Utility function to return the top element in a queue
        fun peek(): Int {
            // check for an empty queue
            if (front == null) {
                println(-1)
            }
            return front?.data ?: -1
        }

        // Utility function to check if the queue is empty or not
        val isEmpty: Boolean
            get() = rear == null && front == null

        // Function to return the size of the queue
        private fun size(): Int {
            return count
        }
    }
}