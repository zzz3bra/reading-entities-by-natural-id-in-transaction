Example of performance for different Flush Mode setting in JPA

Results:

| Benchmark                                                                              | Mode | Cnt |     Score |      Error | Units |
| -------------------------------------------- | ---- | --- | --------- | ---------- | ----- |
| testBatchReadingAndFlushingInsideTransaction | avgt |   3 |   697,172 | ±  144,382 | ms/op |
| testReadingAndFlushingInsideTransaction      | avgt |   3 | 17789,921 | ± 8093,585 | ms/op |
| testReadingAndNoFlushingInsideTransaction    | avgt |   3 |   240,399 | ±    9,044 | ms/op |

Using JMH inside Spring Boot tests credits:
https://gist.github.com/msievers/ce80d343fc15c44bea6cbb741dde7e45
