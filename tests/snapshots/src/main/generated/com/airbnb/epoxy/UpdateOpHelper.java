package com.airbnb.epoxy;

import com.airbnb.epoxy.UpdateOp.Type;
//     ^^^ reference com/
//         ^^^^^^ reference com/airbnb/
//                ^^^^^ reference com/airbnb/epoxy/
//                      ^^^^^^^^ reference com/airbnb/epoxy/UpdateOp/
//                               ^^^^ reference com/airbnb/epoxy/UpdateOp/Type#

import java.util.ArrayList;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^^^^^^ reference java/util/ArrayList#
import java.util.List;
//     ^^^^ reference java/
//          ^^^^ reference java/util/
//               ^^^^ reference java/util/List#

import androidx.annotation.Nullable;
//     ^^^^^^^^ reference androidx/
//              ^^^^^^^^^^ reference androidx/annotation/
//                         ^^^^^^^^ reference androidx/annotation/Nullable#

import static com.airbnb.epoxy.UpdateOp.ADD;
//            ^^^ reference com/
//                ^^^^^^ reference com/airbnb/
//                       ^^^^^ reference com/airbnb/epoxy/
//                             ^^^^^^^^ reference com/airbnb/epoxy/UpdateOp#
import static com.airbnb.epoxy.UpdateOp.MOVE;
//            ^^^ reference com/
//                ^^^^^^ reference com/airbnb/
//                       ^^^^^ reference com/airbnb/epoxy/
//                             ^^^^^^^^ reference com/airbnb/epoxy/UpdateOp#
import static com.airbnb.epoxy.UpdateOp.REMOVE;
//            ^^^ reference com/
//                ^^^^^^ reference com/airbnb/
//                       ^^^^^ reference com/airbnb/epoxy/
//                             ^^^^^^^^ reference com/airbnb/epoxy/UpdateOp#
import static com.airbnb.epoxy.UpdateOp.UPDATE;
//            ^^^ reference com/
//                ^^^^^^ reference com/airbnb/
//                       ^^^^^ reference com/airbnb/epoxy/
//                             ^^^^^^^^ reference com/airbnb/epoxy/UpdateOp#

/** Helper class to collect changes in a diff, batching when possible. */
class UpdateOpHelper {
^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#`<init>`().
^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#
  final List<UpdateOp> opList = new ArrayList<>();
//      ^^^^ reference java/util/List#
//           ^^^^^^^^ reference _root_/
//                     ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#opList.
//                              ^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+1).
//                                  ^^^^^^^^^ reference java/util/ArrayList#
  // We have to be careful to update all item positions in the list when we
  // do a MOVE. This adds some complexity.
  // To do this we keep track of all moves and apply them to an item when we
  // need the up to date position
  final List<UpdateOp> moves = new ArrayList<>();
//      ^^^^ reference java/util/List#
//           ^^^^^^^^ reference _root_/
//                     ^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#moves.
//                             ^^^^^^^^^^^^^^^^^ reference java/util/ArrayList#`<init>`(+1).
//                                 ^^^^^^^^^ reference java/util/ArrayList#
  private UpdateOp lastOp;
//        ^^^^^^^^ reference _root_/
//                 ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#lastOp.
  private int numInsertions;
//            ^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#numInsertions.
  private int numInsertionBatches;
//            ^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#numInsertionBatches.
  private int numRemovals;
//            ^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#numRemovals.
  private int numRemovalBatches;
//            ^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#numRemovalBatches.

  void reset() {
//     ^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#reset().
    opList.clear();
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#opList.
//         ^^^^^ reference java/util/List#clear().
    moves.clear();
//  ^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#moves.
//        ^^^^^ reference java/util/List#clear().
    lastOp = null;
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
    numInsertions = 0;
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertions.
    numInsertionBatches = 0;
//  ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertionBatches.
    numRemovals = 0;
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovals.
    numRemovalBatches = 0;
//  ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovalBatches.
  }

  void add(int indexToInsert) {
//     ^^^ definition com/airbnb/epoxy/UpdateOpHelper#add().
//             ^^^^^^^^^^^^^ definition local0
    add(indexToInsert, 1);
//  ^^^ reference com/airbnb/epoxy/UpdateOpHelper#add(+1).
//      ^^^^^^^^^^^^^ reference local2
  }

  void add(int startPosition, int itemCount) {
//     ^^^ definition com/airbnb/epoxy/UpdateOpHelper#add(+1).
//             ^^^^^^^^^^^^^ definition local3
//                                ^^^^^^^^^ definition local5
    numInsertions += itemCount;
//  ^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertions.
//                   ^^^^^^^^^ reference local7

    // We can append to a previously ADD batch if the new items are added anywhere in the
    // range of the previous batch batch
    boolean batchWithLast = isLastOp(ADD)
//          ^^^^^^^^^^^^^ definition local8
//                          ^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#isLastOp().
//                                   ^^^ reference _root_/
        && (lastOp.contains(startPosition) || lastOp.positionEnd() == startPosition);
//          ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                 ^^^^^^^^ reference contains#
//                          ^^^^^^^^^^^^^ reference local10
//                                            ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                                                   ^^^^^^^^^^^ reference positionEnd#
//                                                                    ^^^^^^^^^^^^^ reference local11

    if (batchWithLast) {
//      ^^^^^^^^^^^^^ reference local12
      addItemsToLastOperation(itemCount, null);
//    ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                            ^^^^^^^^^ reference local13
    } else {
      numInsertionBatches++;
//    ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertionBatches.
      addNewOperation(ADD, startPosition, itemCount);
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addNewOperation().
//                    ^^^ reference _root_/
//                         ^^^^^^^^^^^^^ reference local14
//                                        ^^^^^^^^^ reference local15
    }
  }

  void update(int indexToChange) {
//     ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#update().
//                ^^^^^^^^^^^^^ definition local16
    update(indexToChange, null);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#update(+1).
//         ^^^^^^^^^^^^^ reference local18
  }

  void update(final int indexToChange, EpoxyModel<?> payload) {
//     ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#update(+1).
//                      ^^^^^^^^^^^^^ definition local19
//                                     ^^^^^^^^^^ reference _root_/
//                                                   ^^^^^^^ definition local21
    if (isLastOp(UPDATE)) {
//      ^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#isLastOp().
//               ^^^^^^ reference _root_/
      if (lastOp.positionStart == indexToChange + 1) {
//        ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//               ^^^^^^^^^^^^^ reference positionStart#
//                                ^^^^^^^^^^^^^ reference local23
        // Change another item at the start of the batch range
        addItemsToLastOperation(1, payload);
//      ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                                 ^^^^^^^ reference local24
        lastOp.positionStart = indexToChange;
//      ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//             ^^^^^^^^^^^^^ reference positionStart#
//                             ^^^^^^^^^^^^^ reference local25
      } else if (lastOp.positionEnd() == indexToChange) {
//               ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                      ^^^^^^^^^^^ reference positionEnd#
//                                       ^^^^^^^^^^^^^ reference local26
        // Add another item at the end of the batch range
        addItemsToLastOperation(1, payload);
//      ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                                 ^^^^^^^ reference local27
      } else if (lastOp.contains(indexToChange)) {
//               ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                      ^^^^^^^^ reference contains#
//                               ^^^^^^^^^^^^^ reference local28
        // This item is already included in the existing batch range, so we don't add any items
        // to the batch count, but we still need to add the new payload
        addItemsToLastOperation(0, payload);
//      ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                                 ^^^^^^^ reference local29
      } else {
        // The item can't be batched with the previous update operation
        addNewOperation(UPDATE, indexToChange, 1, payload);
//      ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addNewOperation(+1).
//                      ^^^^^^ reference _root_/
//                              ^^^^^^^^^^^^^ reference local30
//                                                ^^^^^^^ reference local31
      }
    } else {
      addNewOperation(UPDATE, indexToChange, 1, payload);
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addNewOperation(+1).
//                    ^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^ reference local32
//                                              ^^^^^^^ reference local33
    }
  }

  void remove(int indexToRemove) {
//     ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#remove().
//                ^^^^^^^^^^^^^ definition local34
    remove(indexToRemove, 1);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#remove(+1).
//         ^^^^^^^^^^^^^ reference local36
  }

  void remove(int startPosition, int itemCount) {
//     ^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#remove(+1).
//                ^^^^^^^^^^^^^ definition local37
//                                   ^^^^^^^^^ definition local39
    numRemovals += itemCount;
//  ^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovals.
//                 ^^^^^^^^^ reference local41

    boolean batchWithLast = false;
//          ^^^^^^^^^^^^^ definition local42
    if (isLastOp(REMOVE)) {
//      ^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#isLastOp().
//               ^^^^^^ reference _root_/
      if (lastOp.positionStart == startPosition) {
//        ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//               ^^^^^^^^^^^^^ reference positionStart#
//                                ^^^^^^^^^^^^^ reference local44
        // Remove additional items at the end of the batch range
        batchWithLast = true;
//      ^^^^^^^^^^^^^ reference local45
      } else if (lastOp.isAfter(startPosition)
//               ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                      ^^^^^^^ reference isAfter#
//                              ^^^^^^^^^^^^^ reference local46
          && startPosition + itemCount >= lastOp.positionStart) {
//           ^^^^^^^^^^^^^ reference local47
//                           ^^^^^^^^^ reference local48
//                                        ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                                               ^^^^^^^^^^^^^ reference positionStart#
        // Removes additional items at the start and (possibly) end of the batch
        lastOp.positionStart = startPosition;
//      ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//             ^^^^^^^^^^^^^ reference positionStart#
//                             ^^^^^^^^^^^^^ reference local49
        batchWithLast = true;
//      ^^^^^^^^^^^^^ reference local50
      }
    }

    if (batchWithLast) {
//      ^^^^^^^^^^^^^ reference local51
      addItemsToLastOperation(itemCount, null);
//    ^^^^^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                            ^^^^^^^^^ reference local52
    } else {
      numRemovalBatches++;
//    ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovalBatches.
      addNewOperation(REMOVE, startPosition, itemCount);
//    ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addNewOperation().
//                    ^^^^^^ reference _root_/
//                            ^^^^^^^^^^^^^ reference local53
//                                           ^^^^^^^^^ reference local54
    }
  }

  private boolean isLastOp(@UpdateOp.Type int updateType) {
//                ^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#isLastOp().
//                          ^^^^^^^^ reference UpdateOp/
//                                   ^^^^ reference UpdateOp/Type#
//                                            ^^^^^^^^^^ definition local55
    return lastOp != null && lastOp.type == updateType;
//         ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                           ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//                                  ^^^^ reference type#
//                                          ^^^^^^^^^^ reference local57
  }

  private void addNewOperation(@Type int type, int position, int itemCount) {
//             ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#addNewOperation().
//                              ^^^^ reference _root_/
//                                       ^^^^ definition local58
//                                                 ^^^^^^^^ definition local60
//                                                               ^^^^^^^^^ definition local62
    addNewOperation(type, position, itemCount, null);
//  ^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#addNewOperation(+1).
//                  ^^^^ reference local64
//                        ^^^^^^^^ reference local65
//                                  ^^^^^^^^^ reference local66
  }

  private void addNewOperation(@Type int type, int position, int itemCount,
//             ^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#addNewOperation(+1).
//                              ^^^^ reference _root_/
//                                       ^^^^ definition local67
//                                                 ^^^^^^^^ definition local69
//                                                               ^^^^^^^^^ definition local71
      @Nullable EpoxyModel<?> payload) {
//     ^^^^^^^^ reference androidx/annotation/Nullable#
//              ^^^^^^^^^^ reference _root_/
//                            ^^^^^^^ definition local73
    lastOp = UpdateOp.instance(type, position, itemCount, payload);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//           ^^^^^^^^ reference _root_/
//                    ^^^^^^^^ reference instance#
//                             ^^^^ reference local75
//                                   ^^^^^^^^ reference local76
//                                             ^^^^^^^^^ reference local77
//                                                        ^^^^^^^ reference local78
    opList.add(lastOp);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#opList.
//         ^^^ reference java/util/List#add().
//             ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
  }

  private void addItemsToLastOperation(int numItemsToAdd, EpoxyModel<?> payload) {
//             ^^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#addItemsToLastOperation().
//                                         ^^^^^^^^^^^^^ definition local79
//                                                        ^^^^^^^^^^ reference _root_/
//                                                                      ^^^^^^^ definition local81
    lastOp.itemCount += numItemsToAdd;
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//         ^^^^^^^^^ reference itemCount#
//                      ^^^^^^^^^^^^^ reference local83
    lastOp.addPayload(payload);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
//         ^^^^^^^^^^ reference addPayload#
//                    ^^^^^^^ reference local84
  }

  void move(int from, int to) {
//     ^^^^ definition com/airbnb/epoxy/UpdateOpHelper#move().
//              ^^^^ definition local85
//                        ^^ definition local87
    // We can't batch moves
    lastOp = null;
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#lastOp.
    UpdateOp op = UpdateOp.instance(MOVE, from, to, null);
//  ^^^^^^^^ reference _root_/
//           ^^ definition local89
//                ^^^^^^^^ reference _root_/
//                         ^^^^^^^^ reference instance#
//                                  ^^^^ reference _root_/
//                                        ^^^^ reference local91
//                                              ^^ reference local92
    opList.add(op);
//  ^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#opList.
//         ^^^ reference java/util/List#add().
//             ^^ reference local93
    moves.add(op);
//  ^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#moves.
//        ^^^ reference java/util/List#add().
//            ^^ reference local94
  }

  int getNumRemovals() {
//    ^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#getNumRemovals().
    return numRemovals;
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovals.
  }

  boolean hasRemovals() {
//        ^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#hasRemovals().
    return numRemovals > 0;
//         ^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovals.
  }

  int getNumInsertions() {
//    ^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#getNumInsertions().
    return numInsertions;
//         ^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertions.
  }

  boolean hasInsertions() {
//        ^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#hasInsertions().
    return numInsertions > 0;
//         ^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertions.
  }

  int getNumMoves() {
//    ^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#getNumMoves().
    return moves.size();
//         ^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#moves.
//               ^^^^ reference java/util/List#size().
  }

  int getNumInsertionBatches() {
//    ^^^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#getNumInsertionBatches().
    return numInsertionBatches;
//         ^^^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numInsertionBatches.
  }

  int getNumRemovalBatches() {
//    ^^^^^^^^^^^^^^^^^^^^ definition com/airbnb/epoxy/UpdateOpHelper#getNumRemovalBatches().
    return numRemovalBatches;
//         ^^^^^^^^^^^^^^^^^ reference com/airbnb/epoxy/UpdateOpHelper#numRemovalBatches.
  }
}