.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	add %x0, %x0, %x3
	load %x0, $n, %x4
	load %x0, $n, %x5
	addi %x4, 1, %x4
	add %x0, %x0, %x7
	add %x0, %x0, %x6
loop1:
	subi %x5, 1, %x5
	subi %x4, 1, %x4
	add %x0, %x0, %x7
	bgt %x4, %x6, loop2
	beq %x4, %x6, success
loop2:
	load %x7, $a, %x8
	addi %x7, 1, %x9
	load %x9, $a, %x10
	bgt %x10, %x8, swap
	addi %x7, 1, %x7
	bgt %x7, %x5, loop12
	jmp loop2
swap:
	store %x8, $a, %x9
	store %x10, $a, %x7
	addi %x7, 1, %x7
	bgt %x7, %x5, loop12
	jmp loop2
loop12:
	jmp loop1
success:
	end