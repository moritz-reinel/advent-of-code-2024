#! /usr/bin/env python3

import math

cache = {}

# I did part 2 with a friend for fun, so it's Python because he isn't very familiar with Java :)

def calculate_stones(num: int, iteration: int, max: int) -> int:
    if iteration == max:
        return 1

    if cache.get((num, iteration)) != None:
        return cache[(num, iteration)]

    iteration += 1

    if num == 0:
        result = calculate_stones(1, iteration, max)
        cache[(1, iteration)] = result
        return result
    else:
        digits = math.log(num, 10) // 1 + 1

        if digits % 2 == 0:
            front = num // 10 ** (digits / 2)
            front_result = calculate_stones(front, iteration, max)
            cache[(front, iteration)] = front_result

            back = num % 10 ** (digits / 2)
            back_result = calculate_stones(back, iteration, max)
            cache[(back, iteration)] = back_result

            result = front_result + back_result
            cache[(num, iteration - 1)] = result
            return result
        else:
            result = calculate_stones(num * 2024, iteration, max)
            cache[(num * 2024, iteration)] = result
            return result


if __name__ == "__main__":
    with open("input.txt", "r") as fd:
        input = [int(x) for x in fd.readline().split(" ")]

    result = sum(calculate_stones(x, 0, 75) for x in input)

    print(result)
